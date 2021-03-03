package com.example.collaboard_android.inbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R

class InboxAdapter(private val context: Context) : RecyclerView.Adapter<InboxViewHolder>() {

    var data = mutableListOf<InboxData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_inbox, parent, false)
        return InboxViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        holder.onBind(data[position])

        if (data[position].unread) {
            // 안 읽음 -> 동그라미 보여야 함
            holder.unreadPoint.visibility = View.VISIBLE
        }
        else {
            // 읽음 -> 동그라미 사라져야 함
            holder.unreadPoint.visibility = View.GONE
        }
    }
}