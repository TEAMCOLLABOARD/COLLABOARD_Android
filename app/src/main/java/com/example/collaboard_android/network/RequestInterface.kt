package com.example.collaboard_android.network

import com.example.collaboard_android.boardlist.data.ResponseRepoData
import com.example.collaboard_android.inbox.ResponseNotificationData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RequestInterface {
    // repository 조회
    @Headers("Content-Type: application/json")
    @GET("/user/repos")
    fun getRepo(
            @Header("Authorization") Authorization: String?
    ) : Call<ArrayList<ResponseRepoData.ResponseRepoDataItem>>

    // notification 조회
    @Headers("Content-Type: application/json")
    @GET("/notifications")
    fun getNotification(
            @Header("Authorization") Authorization: String?,
            @Query("all") all: Boolean
    ) : Call<ArrayList<ResponseNotificationData.ResponseNotificationDataItem>>
}