package com.example.collaboard_android.boardlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R
import com.example.collaboard_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_board_list.view.*

class BoardListAdapter(private val context: Context) : RecyclerView.Adapter<BoardListViewHolder>() {

    var data = mutableListOf<BoardListData>()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_board_list, parent, false)
        return BoardListViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        holder.onBind(data[position])

        // > 버튼 클릭 시 해당 보드로 이동
        holder.itemView.button_move.setOnClickListener {
            itemClickListener.onClickItem(it, position)
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}