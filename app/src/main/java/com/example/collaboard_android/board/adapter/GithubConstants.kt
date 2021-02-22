package com.example.collaboard_android.board.adapter

object GithubConstants {
    const val CLIENT_ID = "9e0d954b72f1492f06aa"
    const val CLIENT_SECRET = "444c5fe9de20e987f89947365512e153f6423c5a"
    const val REDIRECT_URI = "https://collaboard-526a0.firebaseapp.com/__/auth/handler"
    const val SCOPE = "read:user,user:email,repo,admin:org,notifications,write:discussion" // 요청하고 싶은 내용
    const val AUTH_URL = "https://github.com/login/oauth/authorize" // 여기에 요청
    const val TOKEN_URL = "https://github.com/login/oauth/access_token"
}