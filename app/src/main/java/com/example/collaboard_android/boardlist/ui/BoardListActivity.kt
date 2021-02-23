package com.example.collaboard_android.boardlist.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.collaboard_android.board.ui.BoardActivity
import com.example.collaboard_android.boardlist.adapter.BoardListAdapter
import com.example.collaboard_android.boardlist.adapter.BoardListData
import com.example.collaboard_android.databinding.ActivityBoardListBinding
import com.example.collaboard_android.util.SharedPreferenceController
import com.google.firebase.database.*
import java.lang.StringBuilder
import java.util.*

class BoardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardListBinding

    private lateinit var boardListAdapter: BoardListAdapter

    private lateinit var currentDate: Calendar

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    private lateinit var USER_NAME: String
    private lateinit var PROFILE_IMG: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setPrefValue()

        setCurrentDateOnTextView()

        initUserProfile()

        setClickListenerOnAddBtn()

        initRecyclerView()

        setKeyListenerOnEditText()
    }

    override fun onResume() {
        super.onResume()
        initValue()
    }

    private fun initValue() {
        binding.etParticipationCode.text.clear()
    }

    private fun setPrefValue() {
        USER_NAME = SharedPreferenceController.getUserName(this).toString()
        PROFILE_IMG = SharedPreferenceController.getProfileImg(this).toString()
    }

    private fun initUserProfile() {
        // 사용자 이름 설정
        val nameString = StringBuilder("Hi, ")
        nameString.append(USER_NAME)
        binding.tvName.text = nameString

        //Todo: 사용자 프로필 이미지 설정
        Glide.with(this)
            .load(PROFILE_IMG)
            .into(binding.imgProfile)
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

    // 참여코드 입력 후 키보드의 엔터 or 완료 버튼 클릭 시
    private fun setKeyListenerOnEditText() {
        binding.etParticipationCode.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                goToBoardActivity()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etParticipationCode.windowToken, 0)
    }

    private fun goToBoardActivity() {
        var boardName = ""
        val boardCode = binding.etParticipationCode.text.toString().trim().toUpperCase(Locale.ROOT)
        databaseReference.child("board").child(boardCode).child("info")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    boardName = snapshot.child("boardName").value.toString()

                    // BoardActivity.kt로 이동
                    val intent = Intent(this@BoardListActivity, BoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("boardName", boardName)
                    intent.putExtra("boardCode", boardCode)
                    intent.putExtra("intentFrom", "BoardListActivity")
                    startActivity(intent)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}