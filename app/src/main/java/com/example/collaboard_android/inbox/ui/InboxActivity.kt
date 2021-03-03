package com.example.collaboard_android.inbox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaboard_android.R
import com.example.collaboard_android.databinding.ActivityInboxBinding
import com.example.collaboard_android.inbox.data.ResponseNotificationData
import com.example.collaboard_android.inbox.adapter.InboxAdapter
import com.example.collaboard_android.inbox.adapter.InboxData
import com.example.collaboard_android.network.RequestToServer
import com.example.collaboard_android.util.SharedPreferenceController
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding

    private lateinit var inboxAdapter: InboxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBackButton()

        initRecyclerView()

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
                        getNotification(false)
                    }
                    1 -> {
                        // All
                        getNotification(true)
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
    }

    private fun getNotification(all: Boolean) {
        RequestToServer.service.getNotification(
                Authorization = "Bearer ${SharedPreferenceController.getAccessToken(this)}",
                all = all
        ).enqueue(object: Callback<ArrayList<ResponseNotificationData.ResponseNotificationDataItem>> {
            override fun onResponse(call: Call<ArrayList<ResponseNotificationData.ResponseNotificationDataItem>>, response: Response<ArrayList<ResponseNotificationData.ResponseNotificationDataItem>>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            Log.d("Inbox-unread", "success : ${response.body()}, message : ${response.message()}")
                            bindNotificationData(response.body())
                        } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ArrayList<ResponseNotificationData.ResponseNotificationDataItem>>, t: Throwable) {
                Log.d("Inbox-unread", "fail : ${t.message}")
            }

        })
    }

    private fun bindNotificationData(data: ArrayList<ResponseNotificationData.ResponseNotificationDataItem>?) {

        scrollTop()

        if (data.isNullOrEmpty()) {
            binding.apply {
                tvNotifyCount.text = "0"
                tvNotifyCount.visibility = View.VISIBLE
                recyclerviewNotify.visibility = View.GONE
                constraintlayoutInboxNothing.visibility = View.VISIBLE
            }
        }
        // 서버로부터 받아온 데이터가 empty가 아닐 때
        else {
            binding.apply {
                tvNotifyCount.text = data.size.toString()
                tvNotifyCount.visibility = View.VISIBLE
                recyclerviewNotify.visibility = View.VISIBLE
                constraintlayoutInboxNothing.visibility = View.GONE
            }

            inboxAdapter.data = mutableListOf()
            for (i in data.indices) {
                inboxAdapter.data.add(
                        InboxData(data[i].repository.full_name,
                        data[i].subject.title,
                        data[i].reason,
                        data[i].subject.type,
                        data[i].unread)
                )
            }
            inboxAdapter.notifyDataSetChanged()
        }
    }

    private fun scrollTop() {
        binding.recyclerviewNotify.smoothScrollToPosition(0)
    }

    private fun initBackButton() {
        binding.imgbtnBack.setOnClickListener {
            finish()
        }
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Log.d("inbox-err", ob.getString("message"))
    }
}