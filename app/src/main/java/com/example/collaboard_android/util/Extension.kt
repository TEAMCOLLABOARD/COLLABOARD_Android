package com.example.collaboard_android.util

import android.annotation.SuppressLint
import android.view.View
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