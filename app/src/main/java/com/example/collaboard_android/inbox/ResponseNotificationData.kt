package com.example.collaboard_android.inbox

class ResponseNotificationData : ArrayList<ResponseNotificationData.ResponseNotificationDataItem>(){
    data class ResponseNotificationDataItem(
        val reason: String,
        val repository: Repository,
        val subject: Subject,
        val unread: Boolean
    )

    data class Repository(
        val full_name: String
    )

    data class Subject(
        val title: String,
        val type: String
    )
}