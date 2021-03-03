package com.example.collaboard_android.boardlist.data

class ResponseRepoData : ArrayList<ResponseRepoData.ResponseRepoDataItem>() {
    data class ResponseRepoDataItem(
        val full_name: String,
        val name: String,
    )
}