package com.example.collaboard_android.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collaboard_android.R

class TaskViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_task, parent, false)) {

    var label: TextView = itemView.findViewById(R.id.tv_label)
    var deadline: TextView = itemView.findViewById(R.id.tv_deadline)
    var content: TextView = itemView.findViewById(R.id.tv_content)
    var profileImg: ImageView = itemView.findViewById(R.id.img_profile)
    var userName: TextView = itemView.findViewById(R.id.tv_user_name)
    var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout_task)

    fun onBind(data: TaskData) {
        label.text = data.label
        deadline.text = data.deadline
        content.text = data.content
        Glide.with(itemView).load(data.profileImg).into(profileImg)
        userName.text = data.userName
    }
}