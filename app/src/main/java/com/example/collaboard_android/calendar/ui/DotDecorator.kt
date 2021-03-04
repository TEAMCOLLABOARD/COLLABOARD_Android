package com.example.collaboard_android.calendar.ui

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class DotDecorator(currentDay: CalendarDay) :
    DayViewDecorator {
    private val deadlineDay = currentDay

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == deadlineDay
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(7F, Color.parseColor("#FF8C00"))) // 날자밑에 점 표시
    }
}