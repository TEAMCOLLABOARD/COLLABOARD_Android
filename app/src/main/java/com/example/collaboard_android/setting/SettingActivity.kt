package com.example.collaboard_android.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivitySettingBinding
import com.example.collaboard_android.util.SharedPreferenceController
import com.example.collaboard_android.util.setHeight

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private lateinit var USER_NAME: String
    private lateinit var UID: String
    private lateinit var PROFILE_IMG: String
    private lateinit var TOKEN: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setConstraintHeight()

        setPrefValue()

        initProfile()

        initLogOutButton()

        initWithdrawalButton()

        initBackButton()
    }

    private fun setConstraintHeight() {
        val height = intent.getIntExtra("height", 0)
        binding.constraintLayout4.setHeight(height)
    }

    private fun setPrefValue() {
        SharedPreferenceController.apply {
            USER_NAME = getUserName(this@SettingActivity).toString()
            UID = getUid(this@SettingActivity).toString()
            PROFILE_IMG = getProfileImg(this@SettingActivity).toString()
            TOKEN = getAccessToken(this@SettingActivity).toString()
        }
    }

    private fun initProfile() {
        binding.tvUserName.text = USER_NAME
        Glide.with(this)
            .load(PROFILE_IMG)
            .into(binding.imgProfile)
    }

    private fun initLogOutButton() {
        binding.constraintlayoutLogout.setOnClickListener {
            val logOutDialog = LogOutDialogFragment()
            logOutDialog.show(supportFragmentManager, "log_out_dialog")
        }
    }

    private fun initWithdrawalButton() {
        binding.constraintlayoutWithdrawal.setOnClickListener {
            val withdrawalDialog = WithdrawalDialogFragment()
            withdrawalDialog.show(supportFragmentManager, "withdrawal_dialog")
        }
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