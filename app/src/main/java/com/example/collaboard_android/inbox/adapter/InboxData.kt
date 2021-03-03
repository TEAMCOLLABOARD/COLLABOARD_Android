package com.example.collaboard_android.inbox.adapter

data class InboxData(
    val repoName: String,
    val title: String,
    val reason: String,
    val type: String,
    val unread: Boolean
        )