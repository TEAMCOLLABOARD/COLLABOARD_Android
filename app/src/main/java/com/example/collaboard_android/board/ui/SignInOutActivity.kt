package com.example.collaboard_android.board.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.collaboard_android.MainActivity
import com.example.collaboard_android.board.adapter.GithubConstants
import com.example.collaboard_android.board.adapter.UserInfo
import com.example.collaboard_android.databinding.ActivitySignInOutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

open class SignInOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInOutBinding

    lateinit var githubAuthURLFull: String
    lateinit var githubdialog: Dialog

    var id = ""
    var displayName = ""
    var email = ""
    var avatar = ""
    var accessToken = ""

    private lateinit var user: DatabaseReference
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance() // 파이어베이스 데이터베이스 연동

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        githubAuthURLFull =
            GithubConstants.AUTH_URL + "?client_id=" + GithubConstants.CLIENT_ID + "&scope=" + GithubConstants.SCOPE + "&redirect_uri=" + GithubConstants.REDIRECT_URI + "&state=" + state

        binding.btnSignIn.setOnClickListener {
            setupGithubWebViewDialog(githubAuthURLFull)
        }
    }

    // dialog에 Github 로그인 페이지 띄우기
    @SuppressLint("SetJavaScriptEnabled")
    fun setupGithubWebViewDialog(url: String) {
        githubdialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = GithubWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        githubdialog.setContentView(webView)
        githubdialog.show()
    }

    // A client to know about WebView navigations
    // For API >= 21
    @Suppress("OverridingDeprecatedMember")
    inner class GithubWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request!!.url.toString().startsWith(GithubConstants.REDIRECT_URI)) {
                handleUrl(request.url.toString())

                // authorization 코드 받은 후 dialog 닫기
                if (request.url.toString().contains("code=")) {
                    githubdialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API <= 19
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(GithubConstants.REDIRECT_URI)) {
                handleUrl(url)

                // authorization 코드 받은 후 dialog 닫기
                if (url.contains("?code=")) {
                    githubdialog.dismiss()
                }
                return true
            }
            return false
        }

        // webView URL에서 access token code 또는 오류 확인
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                val githubCode = uri.getQueryParameter("code") ?: ""
                requestForAccessToken(githubCode)
            }
        }
    }

    fun requestForAccessToken(code: String) {
        val grantType = "authorization_code"
        val postParams =
            "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + GithubConstants.REDIRECT_URI + "&client_id=" + GithubConstants.CLIENT_ID + "&client_secret=" + GithubConstants.CLIENT_SECRET
        GlobalScope.launch(Dispatchers.Default) {
            val url = URL(GithubConstants.TOKEN_URL)
            val httpsURLConnection =
                withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "POST"
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            )
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = true

            val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
            withContext(Dispatchers.IO) {
                outputStreamWriter.write(postParams)
                outputStreamWriter.flush()
            }

            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            withContext(Dispatchers.Main) {
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val accessToken = jsonObject.getString("access_token") // The access token

                // 사용자의 ID, 이름, 성, 프로필 사진 URL 가져오기
                fetchGithubUserProfile(accessToken)
            }
        }
    }

    private fun fetchGithubUserProfile(token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val tokenURLFull =
                "https://api.github.com/user"

            val url = URL(tokenURLFull)
            val httpsURLConnection =
                withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.setRequestProperty("Authorization", "Bearer $token")
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // 기본값: UTF-8
            val jsonObject = JSONTokener(response).nextValue() as JSONObject
            accessToken = token

            // GitHub Id
            val githubId = jsonObject.getInt("id")
            id = githubId.toString()

            // GitHub Display Name
            val githubDisplayName = jsonObject.getString("login")
            displayName = githubDisplayName

            // GitHub Email
            val githubEmail = jsonObject.getString("email")
            email = githubEmail

            // GitHub Profile Avatar URL
            val githubAvatarURL = jsonObject.getString("avatar_url")
            avatar = githubAvatarURL

            // 성공시 다음 액티비티로 이동
            openDetailsActivity()
        }
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(this, MainActivity::class.java)

        // firebase에 사용자 정보 저장(uid, token, userName, profileImg)
        user = database.getReference("users") // DB 테이블 연결
        val userInfo = UserInfo(id, accessToken, displayName, avatar)
        user.push().setValue(userInfo) // 랜덤한 문자열을 key로 할당 후, 목록 생성

        startActivity(myIntent)
    }
}