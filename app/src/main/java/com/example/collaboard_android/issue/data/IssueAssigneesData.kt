package com.example.collaboard_android.issue.data

import com.google.gson.annotations.SerializedName

class IssueAssigneesData : ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>() {
    data class ResponseIssueAssigneesDataItem(
        @SerializedName("login") val login: String
    )
}