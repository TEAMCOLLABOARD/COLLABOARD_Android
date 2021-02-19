package com.example.collaboard_android.boardlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R

class BoardListAdapter(private val context: Context) : RecyclerView.Adapter<BoardListViewHolder>() {

    var data = mutableListOf<BoardListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_board_list, parent, false)
        return BoardListViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}