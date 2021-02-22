package com.example.collaboard_android.boardlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.boardlist.adapter.BoardListAdapter
import com.example.collaboard_android.boardlist.adapter.BoardListData
import com.example.collaboard_android.databinding.ActivityBoardListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.StringBuilder
import java.util.*

class BoardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardListBinding

    private lateinit var boardListAdapter: BoardListAdapter

    private lateinit var currentDate: Calendar

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    private val USER_NAME = "Heewon"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setCurrentDateOnTextView()

        initUserProfile()

        setClickListenerOnAddBtn()

        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initValue()
    }

    private fun initValue() {
        binding.etParticipationCode.text.clear()
        BOARD_NAME = ""
        BOARD_CODE = ""
    }

    private fun initUserProfile() {
        // 사용자 이름 설정
        val nameString = StringBuilder("Hi, ")
        nameString.append(USER_NAME)
        binding.tvName.text = nameString

        //Todo: 사용자 프로필 이미지 설정
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

    private fun setClickListenerOnAddBtn() {
        binding.imgbtnAdd.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        lateinit var BOARD_NAME: String
        lateinit var BOARD_CODE: String
    }
}