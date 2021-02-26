package com.example.collaboard_android.calendar.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.example.collaboard_android.calendar.adapter.DeadlineAdapter
import com.example.collaboard_android.calendar.adapter.DeadlineDTO
import com.example.collaboard_android.databinding.ActivityCalendarBinding
import com.google.firebase.database.*
import java.util.*

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
        deadline = database.getReference("deadline") // DB 테이블 연결

        binding.cvCalendar.isShowDaysOfWeekTitle = false

        // 날짜를 선택한 경우
        binding.cvCalendar.selectionManager = SingleSelectionManager {

            val days: List<Calendar> = binding.cvCalendar.selectedDates
            val calendar: Calendar = days[0]
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val date = year.toString() + "-" + (month + 1).toString() + "-" + day.toString()

            deadline.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    // ArrayList 비워줌
                    deadlineList.clear()

                    for (postSnapshot in dataSnapshot.children) {
                        if (postSnapshot.child("date").value.toString() == date) {
                            val item = postSnapshot.getValue(DeadlineDTO::class.java)
                            if (item != null) {
                                deadlineList.add(item)
                            }
                        }
                    }
                    binding.rvCalendarDeadline.adapter?.notifyDataSetChanged()
                    binding.rvCalendarDeadline.scheduleLayoutAnimation() // 애니메이션
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            binding.rvCalendarDeadline.adapter = DeadlineAdapter(this, deadlineList)
            binding.rvCalendarDeadline.layoutManager = LinearLayoutManager(this)
            binding.rvCalendarDeadline.setHasFixedSize(true) // recyclerview 크기 고정
        }
    }
}