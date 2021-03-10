package com.example.collaboard_android.issue.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.collaboard_android.R

class CreateIssueDialog(context: Context) {

    private lateinit var listener: OKBtnClickedListener
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button
    private lateinit var repoName: TextView

    private val dlg = Dialog(context)

    fun start(repo: String) {

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dlg.setContentView(R.layout.dialog_create_issue)
        dlg.setCancelable(false) // 다이얼로그 바깥쪽 눌렀을때 다이얼그 닫히지 않도록
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.show()

        btnOk = dlg.findViewById(R.id.btn_ok)
        btnCancel = dlg.findViewById(R.id.btn_cancel)
        repoName = dlg.findViewById(R.id.tv_repo_name_dialog)

        repoName.text = repo

        btnOk.setOnClickListener {
            listener.onOkBtnClicked("OK")
            dlg.dismiss()
        }

        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object : OKBtnClickedListener {
            override fun onOkBtnClicked(content: String) {
                listener(content)
            }
        }
    }

    interface OKBtnClickedListener {
        fun onOkBtnClicked(content: String)
    }
}