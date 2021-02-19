package com.example.collaboard_android.boardlist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.boardlist.adapter.BoardListAdapter
import com.example.collaboard_android.boardlist.adapter.BoardListData
import com.example.collaboard_android.databinding.ActivityBoardListBinding
import java.lang.StringBuilder
import java.util.*

class BoardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardListBinding

    private lateinit var boardListAdapter: BoardListAdapter

    private lateinit var currentDate: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setCurrentDateOnTextView()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        boardListAdapter = BoardListAdapter(this)
        binding.recyclerviewBoardList.adapter = boardListAdapter
        binding.recyclerviewBoardList.layoutManager = LinearLayoutManager(this)

        boardListAdapter.data = mutableListOf(
            BoardListData("COLLABOARD", "2 members"),
            BoardListData("MOMO", "4 members"),
            BoardListData("STORM", "3 members")
        )
        boardListAdapter.notifyDataSetChanged()
    }

    private fun setCurrentDateOnTextView() {
        currentDate = Calendar.getInstance()
        val month = currentDate.get(Calendar.MONTH) + 1
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        binding.tvDate.text = getFormedDateString(month, day)
    }

    private fun getFormedDateString(month: Int, day: Int) : String {
        val dayString: String = day.toString()
        val monthString: String = when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            else -> "Dec"
        }
        return "$monthString $dayString"
    }
}