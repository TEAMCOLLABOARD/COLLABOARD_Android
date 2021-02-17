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
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.DialogAddTaskBinding
import java.util.*

class AddTaskDialogFragment : DialogFragment() {

    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    private var selectLabel = -1

    private lateinit var currentDate: Calendar

    private lateinit var year: NumberPicker
    private lateinit var month: NumberPicker
    private lateinit var date: NumberPicker

    private var selectYear = 0
    private var selectMonth = 0
    private var selectDate = 0

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

        initDatePicker()
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

    private fun initDatePicker() {
        year = binding.datePicker.year
        month = binding.datePicker.month
        date = binding.datePicker.date

        setDateRange()

        initDateValue()

        setDatePickerMaxValue()

        setDateValue()

        setPickerLimit()

        setListenerOnDatePicker()
    }

    private fun setDateRange() {
        // 현재 날짜 가져오기
        currentDate = Calendar.getInstance()

        // minValue = 최소 날짜 표시
        year.minValue = currentDate.get(Calendar.YEAR)
        month.minValue = currentDate.get(Calendar.MONTH) + 1
        date.minValue = currentDate.get(Calendar.DAY_OF_MONTH)

        // maxValue = 최대 날짜 표시
        year.maxValue = currentDate.get(Calendar.YEAR) + 1
        month.maxValue = 12
        date.maxValue = 31
    }

    private fun setDatePickerMaxValue() {
        // year에 따라 month maxValue 변경
        if (year.value == currentDate.get(Calendar.YEAR)) {
            month.maxValue = 12
        } else {
            month.maxValue = 12
        }

        // month에 따라 month, date maxValue 변경
        if (month.value == currentDate.get(Calendar.MONTH) + 1) {
            setMonthMax()
        } else {
            setMonthMax()
        }
    }

    private fun initDateValue() {
        year.value = currentDate.get(Calendar.YEAR)
        month.value = currentDate.get(Calendar.MONTH) + 1
        date.value = currentDate.get(Calendar.DAY_OF_MONTH)
    }

    private fun setDateValue() {
        selectYear = year.value
        selectMonth = month.value
        selectDate = date.value
    }

    private fun setPickerLimit() {
        // 순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false
        date.wrapSelectorWheel = false

        // edittext 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        date.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun setListenerOnDatePicker() {
        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->
            if(year.value == currentDate.get(Calendar.YEAR)) {
                setPickerMinMaxValue(true)
            } else {
                setPickerMinMaxValue(false)
            }
            setDateValue()
        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->
            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                            Calendar.MONTH) + 1) {
                // 현재 년도에 현재 날짜일 때
                setPickerMinMaxValue(true)
            } else {
                setPickerMinMaxValue(false)
            }
            setDateValue()
        }

        // date picker change listener
        date.setOnValueChangedListener { _, _, _ ->
            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                            Calendar.MONTH) + 1 && date.value == currentDate.get(Calendar.DAY_OF_MONTH)) {
                // 현재 년도에 현재 날짜일 때
                setPickerMinMaxValue(true)
            } else {
                setPickerMinMaxValue(false)
            }
            setDateValue()
        }
    }

    private fun setPickerMinMaxValue(isCurrent: Boolean) {
        if (isCurrent) {
            month.minValue = currentDate.get(Calendar.MONTH) + 1
            date.minValue = currentDate.get(Calendar.DAY_OF_MONTH)
        } else {
            month.minValue = 1
            date.minValue = 1
        }
        month.maxValue = 12
        setMonthMax()
    }

    // 달 별로 일수 다른거 미리 세팅해둔 함수
    private fun setMonthMax() {
        val month = binding.datePicker.month
        val date = binding.datePicker.date

        when (month.value) {
            2 -> {
                date.maxValue = 29
            }
            4, 6, 9, 11 -> {
                date.maxValue = 30
            }
            1, 3, 5, 7, 8, 10, 12 -> {
                date.maxValue = 31
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
            Log.d("initAddButton-date", selectYear.toString())
            Log.d("initAddButton-date", selectMonth.toString())
            Log.d("initAddButton-date", selectDate.toString())
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}