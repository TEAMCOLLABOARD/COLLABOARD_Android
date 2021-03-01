package com.example.collaboard_android.issue.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.collaboard_android.MainActivity
import com.example.collaboard_android.databinding.ActivityIssueBinding
import com.example.collaboard_android.issue.network.GitHubService
import com.example.collaboard_android.issue.network.IssueDTO
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IssueActivity : AppCompatActivity() {

    var server: GitHubService? = null
    private lateinit var binding: ActivityIssueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityIssueBinding.inflate(layoutInflater) // inflate 메소드를 호출하여 Binding Class 변수 초기화
        val view = binding.root
        setContentView(view) // binding 변수의 root 뷰를 가져와서 setContentView 메소드의 인자로 전달

        binding.btnCreate.setOnClickListener {

            if (binding.etTitle.text.toString().trim().isNotEmpty()) {
                creatIssue()
            } else {
                Toast.makeText(applicationContext, "title을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun creatIssue() {
        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // retrofit 객체 통해 인터페이스 생성
        server = retrofit.create(GitHubService::class.java)

        // json 타입의 Body 생성
        val jsonObject = JSONObject()
        jsonObject.put("title", binding.etTitle.text.toString())
        jsonObject.put("body", binding.etDescription.text.toString())

        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // 데이터 전송
        server?.createIssueCall(
            "aerimforest",
            "test",
            requestBody
        )
            ?.enqueue(object : Callback<IssueDTO> {
                // 데이터 전송에 실패한 경우
                override fun onFailure(call: Call<IssueDTO>?, t: Throwable?) {
                }

                // 데이터 전송 성공
                override fun onResponse(call: Call<IssueDTO>?, response: Response<IssueDTO>?) {
                    if (response != null) {
                    }
                }
            })

        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
    }
}