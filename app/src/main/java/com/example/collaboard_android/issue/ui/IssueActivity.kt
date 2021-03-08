package com.example.collaboard_android.issue.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivityIssueBinding
import com.example.collaboard_android.issue.data.IssueAssigneesData
import com.example.collaboard_android.issue.data.IssueLabelsData
import com.example.collaboard_android.issue.network.GitHubService
import com.example.collaboard_android.issue.network.IssueDTO
import com.example.collaboard_android.issue.network.RequestIssueAssignees
import com.example.collaboard_android.issue.network.RequestIssueLabels
import com.example.collaboard_android.util.SharedPreferenceController
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IssueActivity : AppCompatActivity() {

    private lateinit var labelList: ArrayList<String>
    private lateinit var assigneesList: ArrayList<String>

    private lateinit var binding: ActivityIssueBinding

    private var owner = ""
    private var repo = ""
    var selectedLabels = ""
    var selectedAssignees = ""
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityIssueBinding.inflate(layoutInflater) // inflate 메소드를 호출하여 Binding Class 변수 초기화
        val view = binding.root
        setContentView(view) // binding 변수의 root 뷰를 가져와서 setContentView 메소드의 인자로 전달

        // BoardActivity.kt에서 intent 값 받아오기
        owner = intent.getStringExtra("owner").toString()
        repo = intent.getStringExtra("repo").toString()

        println("owner: $owner")
        println("repo: $repo")

        binding.btnCreate.setOnClickListener {

            if (binding.etTitle.text.toString().trim().isNotEmpty()) {
                creatIssue()
            } else {
                Toast.makeText(applicationContext, "title을 입력해주세요", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // 백버튼 클릭
        binding.imgbtnBackIssue.setOnClickListener {
            finish()
        }

        retrofit = retrofitBuilder()
        getIssueLabels()
        getIssueAssignees()
    }

    // Retrofit 객체 생성
    private fun retrofitBuilder(): Retrofit {
        // Retrofit 객체 생성
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getIssueLabels() {

        labelList = ArrayList()

        // retrofit 객체 통해 인터페이스 생성
        val server: RequestIssueLabels = retrofit.create(RequestIssueLabels::class.java)

        // 데이터 전송
        server.getIssueLabels(
            "Bearer ${SharedPreferenceController.getAccessToken(this)}",
            owner, repo
        )
            .enqueue(object : Callback<ArrayList<IssueLabelsData.ResponseIssueLabelsDataItem>> {
                // 데이터 전송에 실패한 경우
                override fun onFailure(
                    call: Call<ArrayList<IssueLabelsData.ResponseIssueLabelsDataItem>>?,
                    t: Throwable?
                ) {
                }

                // 데이터 전송 성공
                override fun onResponse(
                    call: Call<ArrayList<IssueLabelsData.ResponseIssueLabelsDataItem>>,
                    response: Response<ArrayList<IssueLabelsData.ResponseIssueLabelsDataItem>>
                ) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {

                            for (i in 0 until it.size) {
                                labelList.add(it[i].name)
                            }
                            labelsAdapter()

                        } ?: showError(response.errorBody())
                }
            })
    }

    private fun labelsAdapter() {

        val labelsAdapter = ArrayAdapter(this, R.layout.item_issue_label_spinner, labelList)

        binding.constraintlayoutLabelsSpinner.visibility = View.VISIBLE
        binding.spinnerLabels.adapter = labelsAdapter
        binding.spinnerLabels.setSelection(0)

        binding.spinnerLabels.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedLabels = labelList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getIssueAssignees() {
        assigneesList = ArrayList()

        // retrofit 객체 통해 인터페이스 생성
        val server: RequestIssueAssignees = retrofit.create(RequestIssueAssignees::class.java)

        // 데이터 전송
        server.getIssueAssignees(
            "Bearer ${SharedPreferenceController.getAccessToken(this)}",
            owner, repo
        )
            .enqueue(object :
                Callback<ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>> {
                // 데이터 전송에 실패한 경우
                override fun onFailure(
                    call: Call<ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>>?,
                    t: Throwable?
                ) {
                }

                // 데이터 전송 성공
                override fun onResponse(
                    call: Call<ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>>,
                    response: Response<ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>>
                ) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {

                            for (i in 0 until it.size) {
                                assigneesList.add(it[i].login)
                            }
                            assigneesAdapter()

                        } ?: showError(response.errorBody())
                }
            })
    }

    private fun assigneesAdapter() {
        val assigneesAdapter = ArrayAdapter(this, R.layout.item_label_spinner, assigneesList)

        binding.constraintlayoutAssigneesSpinner.visibility = View.VISIBLE
        binding.spinnerAssignees.adapter = assigneesAdapter
        binding.spinnerAssignees.setSelection(0)

        binding.spinnerAssignees.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedAssignees = assigneesList[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun creatIssue() {

        // retrofit 객체 통해 인터페이스 생성
        val server: GitHubService = retrofit.create(GitHubService::class.java)

        // json 타입의 Body 생성
        val jsonObject = JSONObject()
        val labelsArray = JSONArray()
        val assigneesArray = JSONArray()

        jsonObject.put("title", binding.etTitle.text.toString())
        jsonObject.put("body", binding.etDescription.text.toString())

        labelsArray.put(selectedLabels)
        jsonObject.put("labels", labelsArray)

        assigneesArray.put(selectedAssignees)
        jsonObject.put("assignees", assigneesArray)

        val requestBody: RequestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        // 데이터 전송
        server.createIssueCall(
            "Bearer ${SharedPreferenceController.getAccessToken(this)}",
            owner, repo, requestBody
        )
            .enqueue(object : Callback<IssueDTO> {
                // 데이터 전송에 실패한 경우
                override fun onFailure(call: Call<IssueDTO>?, t: Throwable?) {
                }

                // 데이터 전송 성공
                override fun onResponse(call: Call<IssueDTO>, response: Response<IssueDTO>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            Log.d(
                                "issueActivity",
                                "success: ${it.title}, message: ${response.message()}"
                            )
                            Toast.makeText(
                                this@IssueActivity,
                                "Issue created successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } ?: showError(response.errorBody())
                }
            })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Log.d("issueActivity-error", ob.getString("message"))
    }
}