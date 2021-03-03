package com.example.collaboard_android.inbox

data class InboxData(
    val repoName: String,
    val title: String,
    val reason: String,
    val type: String,
    val unread: Boolean
        )