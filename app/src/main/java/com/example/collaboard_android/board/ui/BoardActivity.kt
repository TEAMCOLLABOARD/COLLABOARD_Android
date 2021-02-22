package com.example.collaboard_android.board.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.ViewPagerAdapter
import com.example.collaboard_android.databinding.ActivityBoardBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var BOARD_NAME: String
    private lateinit var BOARD_CODE: String

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mContext = this

        initValue()

        getIntentValue()

        initViewPager()

        setViewPagerPaging()
    }

    private fun initValue() {
        frag_board_name = ""
        frag_board_code = ""
    }

    private fun getIntentValue() {
        val intentFrom = intent.getStringExtra("intentFrom").toString()
        Log.d("getIntentValue", intentFrom)
        when (intentFrom) {
            "BoardListActivity" -> {
                setBoardInfo()
            }
            "ShowPartCodeDialogFragment" -> {
                setBoardInfo()
            }
            else -> {
                BOARD_NAME = "error"
                BOARD_CODE = "error"
            }
        }
        initBoardName()
    }

    private fun setBoardInfo() {
        BOARD_NAME = intent.getStringExtra("boardName").toString()
        BOARD_CODE = intent.getStringExtra("boardCode").toString()
        frag_board_name = BOARD_NAME
        frag_board_code = BOARD_CODE
    }

    private fun initBoardName() {
        binding.tvRepoName.text = BOARD_NAME
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.fragments = listOf(
            TodoFragment(),
            InProgressFragment(),
            DoneFragment()
        )
        binding.viewpagerBoard.adapter = viewPagerAdapter
        binding.viewpagerBoard.clipToPadding = false
    }

    private fun setViewPagerPaging() {
        val dpValue = 14
        val d: Float = resources.displayMetrics.density
        val margin = (dpValue * d).toInt()
        
        binding.viewpagerBoard.setPadding(margin, 0, margin, 0)
        binding.viewpagerBoard.pageMargin = (margin / 1.7).toInt()
    }

    fun getCurrentFrag() : Int {
        return when (binding.viewpagerBoard.currentItem) {
            0 -> 0
            1 -> 1
            else -> 2
        }
    }

    fun movePage(index: Int) {
        val handler = Handler()
        handler.postDelayed({
            binding.viewpagerBoard.post {
                binding.viewpagerBoard.setCurrentItem(index, true)
            }
        }, 500)
    }

    fun putTodoTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("todo")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    fun putInProgressTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("inProgress")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    fun putDoneTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("done")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    companion object {
        lateinit var mContext: BoardActivity
            private set

        var frag_board_name = ""
        var frag_board_code = ""
    }
}