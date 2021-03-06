package com.example.collaboard_android.calendar.ui

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == CalendarDay.today()
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#EB0000")))
    }
}