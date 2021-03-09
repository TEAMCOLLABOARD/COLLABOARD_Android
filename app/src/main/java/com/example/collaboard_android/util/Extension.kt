package com.example.collaboard_android.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*

/* 확장함수 */

/* label의 index를 string으로 변환해주는 함수 */
fun getLabelString(label: Int) : String {
    return when (label) {
        0 -> "Feature"
        1 -> "Fix"
        2 -> "Network"
        3 -> "Refactor"
        4 -> "Chore"
        5 -> "Style"
        else -> "error"
    }
}

/* yyyy-mm-dd 형식의 날짜를 year, month, day 형식으로 반환해주는 함수 */
fun getYMDString(date: String) : IntArray {
    val splitString = date.split("-")
    val year = splitString[0].toInt()
    val month = splitString[1].toInt()
    val day = splitString[2].toInt()

    return intArrayOf(year, month, day)
}

/* d-day 계산 함수 */
@SuppressLint("SimpleDateFormat")
fun calDeadline(pickDate: IntArray) : Int {
    val year = pickDate[0]
    val month = pickDate[1] - 1
    val date = pickDate[2]

    try {
        val todayCalendar = Calendar.getInstance()
        val dDayCalendar = Calendar.getInstance()

        dDayCalendar.set(year, month, date)

        val today: Long = todayCalendar.timeInMillis / 86400000
        val dDay: Long = dDayCalendar.timeInMillis / 86400000
        val count: Long = dDay - today

        return count.toInt()
    }
    catch (e: Exception) {
        e.printStackTrace()
        return -1
    }
}

/* 계산한 d-day를 형식에 맞춰 string으로 반환해주는 함수 */
fun getDeadlineString(deadline: Int) : String {
    return when {
        deadline > 0 -> {
            "D-$deadline"
        }
        deadline == 0 -> {
            "D-Day"
        }
        else -> {
            val result = (-1) * deadline
            "D+$result"
        }
    }
}

/* view height 설정 및 변경 함수 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun showKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun hideKeyboard(context: Context, editText: EditText) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}