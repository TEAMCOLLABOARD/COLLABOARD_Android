package com.example.collaboard_android.issue.network

import com.example.collaboard_android.issue.data.IssueAssigneesData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface RequestIssueAssignees {
    // assignees
    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}/assignees")
    fun getIssueAssignees(
        @Header("Authorization") Authorization: String?,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<ArrayList<IssueAssigneesData.ResponseIssueAssigneesDataItem>>
}