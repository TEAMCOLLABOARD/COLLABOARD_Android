package com.example.collaboard_android.inbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivityInboxBinding

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding

    private lateinit var inboxAdapter: InboxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSpinner()
    }

    private fun initSpinner() {
        val item = resources.getStringArray(R.array.inbox_array)

        val spinnerAdapter = ArrayAdapter(this, R.layout.item_label_spinner, item)
        binding.spinner.adapter = spinnerAdapter

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        // Unread
                        binding.recyclerviewNotify.visibility = View.VISIBLE
                        binding.constraintlayoutInboxNothing.visibility = View.GONE
                        initRecyclerView()
                    }
                    1 -> {
                        // All
                        binding.recyclerviewNotify.visibility = View.GONE
                        binding.constraintlayoutInboxNothing.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun initRecyclerView() {
        inboxAdapter = InboxAdapter(this)
        binding.recyclerviewNotify.adapter = inboxAdapter
        binding.recyclerviewNotify.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)

        inboxAdapter.data = mutableListOf(
            InboxData("Team-MoMo/MoMo_Android",
                "[fix] activity_setting.xml 파일의 switch 색상 변경 feature/#298",
                "Comment",
                "Pull request",
                false),
            InboxData("Team-MoMo/MoMo_Android",
                "[fix] activity_setting.xml 파일의 switch 색상 변경 feature/#298",
                "Comment",
                "Pull request",
                false),
            InboxData("Team-MoMo/MoMo_Android",
                "[fix] activity_setting.xml 파일의 switch 색상 변경 feature/#298",
                "Comment",
                "Pull request",
                false),
            InboxData("Team-MoMo/MoMo_Android",
                "[fix] activity_setting.xml 파일의 switch 색상 변경 feature/#298",
                "Comment",
                "Pull request",
                false),
        )
        inboxAdapter.notifyDataSetChanged()
    }
}