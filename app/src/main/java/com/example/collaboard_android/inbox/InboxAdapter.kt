package com.example.collaboard_android.inbox

import android.content.Context
import android.view.LayoutInflater
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
    }
}