package com.example.collaboard_android.issue.network

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class IssueDTO(
    @SerializedName("url") val url: String,
    @SerializedName("repository_url") val repository_url: String,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)

// API interface
// HTTP API를 interface 형태로 생성
interface GitHubService {
    @Headers(
        "Authorization: token 1d6eb534859d0829dd29345eb3e5b5043fac7bc2",
        "Content-Type: application/json"
    )
    @POST("/repos/{owner}/{repo}/issues")

    fun createIssueCall(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body requestBody: RequestBody
    ): Call<IssueDTO>
}
