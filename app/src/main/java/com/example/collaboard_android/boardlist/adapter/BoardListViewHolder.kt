package com.example.collaboard_android.boardlist.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R

class BoardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val boardName: TextView = itemView.findViewById(R.id.tv_board_name)
    private val memberCount: TextView = itemView.findViewById(R.id.tv_member_count)

    fun onBind(data: BoardListData) {
        boardName.text = data.boardName
        memberCount.text = data.memberCount
    }
}