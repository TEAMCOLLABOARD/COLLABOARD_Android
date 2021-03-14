package com.example.collaboard_android.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.collaboard_android.databinding.ActivitySplashBinding
import com.example.collaboard_android.login.ui.SignInOutActivity

class SplashActivity : Activity() {

    private lateinit var binding: ActivitySplashBinding

    private val SPLASH_DISPLAY_LENGTH = 5305

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSplashAnimation()
    }

    private fun initSplashAnimation() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, SignInOutActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}