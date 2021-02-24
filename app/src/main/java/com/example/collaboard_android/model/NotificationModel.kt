package com.example.collaboard_android.model

class NotificationModel {
    var to: String? = null
    var notification = Notification()
    var data = Data()

    inner class Notification {
        lateinit var title: String
        lateinit var text: String
    }

    // foreground push
    inner class Data {
        lateinit var title: String
        lateinit var text: String
    }
}