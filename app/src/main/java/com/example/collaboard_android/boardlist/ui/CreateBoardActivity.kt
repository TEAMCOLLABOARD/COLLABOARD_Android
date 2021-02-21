package com.example.collaboard_android.boardlist.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivityCreateBoardBinding

class CreateBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBoardBinding

    private var selectRepo = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nContext = this

        setKeyListenerOnEditText()

        initEditImageButton()

        initRepoSpinner()

        initCreateButton()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditImageButton() {
        binding.imgbtnEdit.setOnClickListener {
            setEditTextEnabled()
            showKeyboard()
        }
        binding.etBoardName.setOnTouchListener { _, _ ->
            setEditTextEnabled()
            showKeyboard()
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

    private fun showKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setKeyListenerOnEditText() {
        binding.etBoardName.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                setEditTextDisabled()
                hideKeyboard()
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

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etBoardName.windowToken, 0)
    }

    private fun initRepoSpinner() {
        val item = resources.getStringArray(R.array.label_array)

        val repoAdapter = ArrayAdapter(this, R.layout.item_repo_spinner, item)
        binding.spinnerRepo.adapter = repoAdapter

        binding.spinnerRepo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Todo: 사용자의 repo 목록 추가
                selectRepo = when (position) {
                    0 -> 0 // feature
                    1 -> 1 // fix
                    2 -> 2 // network
                    3 -> 3 // refactor
                    4 -> 4 // chore
                    5 -> 5 // style
                    else -> -1
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun initCreateButton() {
        binding.btnCreate.setOnClickListener {
            val partCodeDialog = ShowPartCodeDialogFragment()
            partCodeDialog.show(supportFragmentManager, "show_part_code_dialog")
        }
    }

    fun finishActivity() {
        finish()
    }

    companion object {
        lateinit var nContext: CreateBoardActivity
        private set
    }
}