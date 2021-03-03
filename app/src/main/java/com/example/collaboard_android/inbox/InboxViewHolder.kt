package com.example.collaboard_android.inbox

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R

class InboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var repoName: TextView = itemView.findViewById(R.id.tv_repo_name_inbox)
    var title: TextView = itemView.findViewById(R.id.tv_title_inbox)
    var reason: TextView = itemView.findViewById(R.id.tv_label_reason)
    var type: TextView = itemView.findViewById(R.id.tv_label_type)
    var unreadPoint: View = itemView.findViewById(R.id.view_unread_notify)

    fun onBind(data: InboxData) {
        repoName.text = data.repoName
        title.text = data.title
        reason.text = data.reason
        type.text = data.type
    }
}