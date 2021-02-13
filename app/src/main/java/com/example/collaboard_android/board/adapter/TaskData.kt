package com.example.collaboard_android.board.adapter

import android.graphics.drawable.Drawable

data class TaskData(
        val label: String,
        val deadline: String,
        val content: String,
        val profileImg: Drawable?,
        val userName: String
)
