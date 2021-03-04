package com.example.collaboard_android.calendar.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.calendar.adapter.DeadlineAdapter
import com.example.collaboard_android.calendar.adapter.DeadlineDTO
import com.example.collaboard_android.databinding.ActivityCalendarBinding
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CalendarActivity : AppCompatActivity() {

    private var deadlineList = arrayListOf<DeadlineDTO>()

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var deadline: DatabaseReference

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityCalendarBinding.inflate(layoutInflater) // inflate 메소드를 호출하여 Binding Class 변수 초기화
        val view = binding.root
        setContentView(view) // binding 변수의 root 뷰를 가져와서 setContentView 메소드의 인자로 전달

        val database: FirebaseDatabase = FirebaseDatabase.getInstance() // 파이어베이스 데이터베이스 연동

        // Todo: boardActivity에서 intent로 uid 받아와 query 수정 -> "users/uid/deadline/currentBoard"
        deadline = database.getReference("users/52696359/deadline/Q7Z9L6") // DB 테이블 연결

        binding.rvCalendarDeadline.adapter = DeadlineAdapter(this, deadlineList)
        binding.rvCalendarDeadline.layoutManager = LinearLayoutManager(this)
        binding.rvCalendarDeadline.setHasFixedSize(true) // recyclerview 크기 고정

        val calList = ArrayList<CalendarDay>()
        calList.add(CalendarDay.from(2021, 3, 3))
        calList.add(CalendarDay.from(2021, 3, 10))
        calList.add(CalendarDay.from(2021, 3, 21))
        calList.add(CalendarDay.from(2021, 3, 25))
        for (calDay in calList) {

        }

        // 날짜 클릭
        binding.mcvCalendar.setOnDateChangedListener { widget, date, selected ->
            year = date.year
            month = date.month
            day = date.day
            val selectedDate = "$year-$month-$day"

            deadline.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    // ArrayList 비워줌
                    deadlineList.clear()

                    // 선택한 날짜와 동일한 date 값을 가진 데이터 불러옴
                    for (postSnapshot in dataSnapshot.children) {
                        if (postSnapshot.child("date").value.toString() == selectedDate) {
                            val item = postSnapshot.getValue(DeadlineDTO::class.java)
                            if (item != null) {
                                deadlineList.add(item)
                            }
                        }
                    }
                    binding.rvCalendarDeadline.adapter?.notifyDataSetChanged()

                    binding.splDeadline.addPanelSlideListener(object :
                        SlidingUpPanelLayout.PanelSlideListener {

                        override fun onPanelSlide(panel: View?, slideOffset: Float) {
                            binding.rvCalendarDeadline.scheduleLayoutAnimation() // item 애니메이션
                        }

                        override fun onPanelStateChanged(
                            panel: View?,
                            previousState: SlidingUpPanelLayout.PanelState?,
                            newState: SlidingUpPanelLayout.PanelState?
                        ) {
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}