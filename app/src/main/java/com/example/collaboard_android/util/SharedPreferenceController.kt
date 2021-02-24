package com.example.collaboard_android.util

import android.content.Context

object SharedPreferenceController {

    private val TOKEN = "TOKEN"
    private val UID = "UID"
    private val USER_NAME = "USER_NAME"
    private val PROFILE_IMG = "PROFILE_IMG"
    private val PUSH_TOKEN = "PUSH_TOKEN"

    // token
    fun setAccessToken(context: Context, authorization: String?) {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("TOKEN", authorization)
        editor.apply()
    }

    fun getAccessToken(context: Context) : String? {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        return pref.getString("TOKEN", "")
    }

    fun clearAccessToken(context: Context) {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // uid
    fun setUid(context: Context, uid: String) {
        val pref = context.getSharedPreferences(UID, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("UID", uid)
        editor.apply()
    }

    fun getUid(context: Context) : String? {
        val pref = context.getSharedPreferences(UID, Context.MODE_PRIVATE)
        return pref.getString("UID", "")
    }

    fun clearUid(context: Context) {
        val pref = context.getSharedPreferences(UID, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // user name
    fun setUserName(context: Context, userName: String?) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("USER_NAME", userName)
        editor.apply()
    }

    fun getUserName(context: Context) : String? {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        return pref.getString("USER_NAME", "")
    }

    fun clearUserName(context: Context) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // profile image
    fun setProfileImg(context: Context, img: String?) {
        val pref = context.getSharedPreferences(PROFILE_IMG, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("PROFILE_IMG", img)
        editor.apply()
    }

    fun getProfileImg(context: Context) : String? {
        val pref = context.getSharedPreferences(PROFILE_IMG, Context.MODE_PRIVATE)
        return pref.getString("PROFILE_IMG", "")
    }

    fun clearProfileImg(context: Context) {
        val pref = context.getSharedPreferences(PROFILE_IMG, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // push token
    fun setPushToken(context: Context, pushToken: String?) {
        val pref = context.getSharedPreferences(PUSH_TOKEN, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("PUSH_TOKEN", pushToken)
        editor.apply()
    }

    fun getPushToken(context: Context) : String? {
        val pref = context.getSharedPreferences(PUSH_TOKEN, Context.MODE_PRIVATE)
        return pref.getString("PUSH_TOKEN", "")
    }

    // 전체 삭제
    fun clearAll(context: Context) {
        clearAccessToken(context)
        clearUid(context)
        clearUserName(context)
        clearProfileImg(context)
    }
}