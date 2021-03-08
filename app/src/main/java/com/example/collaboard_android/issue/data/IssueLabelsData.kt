package com.example.collaboard_android.issue.data

import com.google.gson.annotations.SerializedName

class IssueLabelsData : ArrayList<IssueLabelsData.ResponseIssueLabelsDataItem>() {
    data class ResponseIssueLabelsDataItem(
        @SerializedName("name") val name: String
    )
}