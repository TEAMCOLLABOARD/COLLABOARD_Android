package com.example.collaboard_android.board.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.DialogAddTaskBinding

class AddTaskDialogFragment : DialogFragment() {

    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    private var selectLabel = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCloseButton()

        initAddButton()

        setKeyListenerOnEditText()

        initEditImageButton()

        initLabelSpinner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEditImageButton() {
        binding.imgbtnEdit.setOnClickListener {
            setEditTextEnabled()
            showKeyboard()
        }
        binding.etDescription.setOnTouchListener { _, _ ->
            setEditTextEnabled()
            showKeyboard()
            true
        }
    }

    private fun setEditTextEnabled() {
        binding.etDescription.apply {
            requestFocus()
            isFocusable = true
            isCursorVisible = true
            isCursorVisible = true
            setSelection(this.length())
        }
        binding.viewUnderline.setBackgroundColor(ContextCompat.getColor(context!!, R.color.blue_main))
    }

    private fun showKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setKeyListenerOnEditText() {
        binding.etDescription.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                setEditTextDisabled()
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun setEditTextDisabled() {
        binding.etDescription.apply {
            setTextColor(ContextCompat.getColor(context!!, R.color.black_description))
            clearFocus()
            isCursorVisible = false
        }
        binding.viewUnderline.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray_divider))
    }

    private fun hideKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etDescription.windowToken, 0)
    }

    private fun initLabelSpinner() {
        val item = resources.getStringArray(R.array.label_array)

        val labelAdapter = ArrayAdapter(context!!, R.layout.item_label_spinner, item)
        binding.spinnerLabel.adapter = labelAdapter

        binding.spinnerLabel.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectLabel = when (position) {
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

    private fun initCloseButton() {
        binding.buttonCancel.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initAddButton() {
        binding.buttonAdd.setOnClickListener {
            Log.d("initAddButton", selectLabel.toString())
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}