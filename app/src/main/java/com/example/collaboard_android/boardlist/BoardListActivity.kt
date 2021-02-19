package com.example.collaboard_android.boardlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.boardlist.adapter.BoardListAdapter
import com.example.collaboard_android.boardlist.adapter.BoardListData
import com.example.collaboard_android.databinding.ActivityBoardListBinding

class BoardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardListBinding

    private lateinit var boardListAdapter: BoardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
}