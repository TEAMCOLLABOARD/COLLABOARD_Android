package com.example.collaboard_android.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBackButton()
    }

    private fun initBackButton() {
        binding.imgbtnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }
}