package com.example.collaboard_android.boardlist.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.collaboard_android.R
import com.example.collaboard_android.boardlist.data.ResponseRepoData
import com.example.collaboard_android.databinding.ActivityCreateBoardBinding
import com.example.collaboard_android.network.RequestToServer
import com.example.collaboard_android.util.SharedPreferenceController
import com.example.collaboard_android.util.hideKeyboard
import com.example.collaboard_android.util.showKeyboard
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBoardBinding

    private var selectRepo = ""
    private var selectRepoFullName = ""

    private lateinit var repoList: ArrayList<String>
    private lateinit var repoFullNameList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nContext = this

        initCompanionValue()

        initBackButton()

        getRepoList()

        setKeyListenerOnEditText()

        initEditImageButton()

        initCreateButton()
    }

    private fun initCompanionValue() {
        dialog_board_name = ""
        dialog_repo_name = ""
        dialog_repo_full_name = ""
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditImageButton() {
        binding.imgbtnEdit.setOnClickListener {
            setEditTextEnabled()
            showKeyboard(this)
        }
        binding.etBoardName.setOnTouchListener { _, _ ->
            setEditTextEnabled()
            showKeyboard(this)
            true
        }
    }

    private fun setEditTextEnabled() {
        binding.etBoardName.apply {
            requestFocus()
            isFocusable = true
            isCursorVisible = true
            isCursorVisible = true
            setSelection(this.length())
        }
    }

    private fun setKeyListenerOnEditText() {
        binding.etBoardName.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                setEditTextDisabled()
                hideKeyboard(this, binding.etBoardName)
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setEditTextDisabled() {
        binding.etBoardName.apply {
            clearFocus()
            isCursorVisible = false
        }
    }

    private fun getRepoList() {
        repoList = ArrayList()
        repoFullNameList = ArrayList()

        RequestToServer.service.getRepo(
                Authorization = "Bearer ${SharedPreferenceController.getAccessToken(this)}"
        ).enqueue(object: Callback<ArrayList<ResponseRepoData.ResponseRepoDataItem>> {
            override fun onResponse(call: Call<ArrayList<ResponseRepoData.ResponseRepoDataItem>>,
                                    response: Response<ArrayList<ResponseRepoData.ResponseRepoDataItem>>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            Log.d("CreateBoard-repo", "success : ${response.body()}, message : ${response.message()}")

                            for (i in 0 until it.size) {
                                repoList.add(it[i].name)
                                repoFullNameList.add(it[i].full_name)
                            }
                            initRepoSpinner()

                        } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ArrayList<ResponseRepoData.ResponseRepoDataItem>>, t: Throwable) {
                Log.d("CreateBoard-repo", "fail : ${t.message}")
            }

        })
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Log.d("CreateBoard-repo-err", ob.getString("message"))
    }

    private fun initRepoSpinner() {
        binding.constraintlayoutSpinner.visibility = View.VISIBLE
        val repoAdapter = ArrayAdapter(this, R.layout.item_repo_spinner, repoList)
        binding.spinnerRepo.adapter = repoAdapter
        binding.spinnerRepo.setSelection(0)

        binding.spinnerRepo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectRepo = repoList[position]
                selectRepoFullName = repoFullNameList[position]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun initCreateButton() {
        binding.btnCreate.setOnClickListener {
            dialog_board_name = binding.etBoardName.text.toString()
            dialog_repo_name = selectRepo
            dialog_repo_full_name = selectRepoFullName

            if (dialog_board_name == "") {
                Toast.makeText(this, "Enter the board name", Toast.LENGTH_SHORT).show()
            }
            else {
                val partCodeDialog = ShowPartCodeDialogFragment()
                partCodeDialog.show(supportFragmentManager, "show_part_code_dialog")
            }
        }
    }

    private fun initBackButton() {
        binding.imgbtnBack.setOnClickListener {
            finish()
        }
    }

    fun finishActivity() {
        finish()
    }

    companion object {
        lateinit var nContext: CreateBoardActivity
        private set

        var dialog_board_name = ""
        var dialog_repo_name = ""
        var dialog_repo_full_name = ""
    }
}