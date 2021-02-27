package com.example.collaboard_android.board.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.UserInfo
import com.example.collaboard_android.board.adapter.ViewPagerAdapter
import com.example.collaboard_android.calendar.ui.CalendarActivity
import com.example.collaboard_android.databinding.ActivityBoardBinding
import com.example.collaboard_android.model.NotificationModel
import com.example.collaboard_android.util.SharedPreferenceController
import com.google.firebase.database.*
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private lateinit var BOARD_NAME: String
    private lateinit var BOARD_CODE: String

    private lateinit var TOKEN: String
    private lateinit var UID: String
    private lateinit var USER_NAME: String
    private lateinit var PROFILE_IMG: String
    private lateinit var PUSH_TOKEN: String

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mContext = this

        initValue()

        setPrefValue()

        getIntentValue()

        initIssueButton()

        initCalendarButton()

        initBackButton()

        initViewPager()

        setViewPagerPaging()

        passUserInfoToServer()
    }

    private fun initValue() {
        frag_board_name = ""
        frag_board_code = ""
    }

    private fun setPrefValue() {
        SharedPreferenceController.apply {
            TOKEN = getAccessToken(this@BoardActivity).toString()
            UID = getUid(this@BoardActivity).toString()
            USER_NAME = getUserName(this@BoardActivity).toString()
            PROFILE_IMG = getProfileImg(this@BoardActivity).toString()
            PUSH_TOKEN = getPushToken(this@BoardActivity).toString()
        }
    }

    private fun getIntentValue() {
        val intentFrom = intent.getStringExtra("intentFrom").toString()
        Log.d("getIntentValue", intentFrom)
        when (intentFrom) {
            // 보드 리스트의 > 버튼을 눌러 입장하는 경우
            "BoardListActivity" -> {
                setBoardInfo()
            }
            // 새 보드를 만들어서 입장하는 경우
            "ShowPartCodeDialogFragment" -> {
                setBoardInfo()
            }
            // 참여코드를 입력해서 입장하는 경우
            "ParticipationCode" -> {
                setBoardInfo()
            }
            else -> {
                BOARD_NAME = "error"
                BOARD_CODE = "error"
            }
        }
        initBoardName()
    }

    private fun setBoardInfo() {
        BOARD_NAME = intent.getStringExtra("boardName").toString()
        BOARD_CODE = intent.getStringExtra("boardCode").toString()
        frag_board_name = BOARD_NAME
        frag_board_code = BOARD_CODE
    }

    private fun initBoardName() {
        binding.tvRepoName.text = BOARD_NAME
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.fragments = listOf(
            TodoFragment(),
            InProgressFragment(),
            DoneFragment()
        )
        binding.viewpagerBoard.adapter = viewPagerAdapter
        binding.viewpagerBoard.clipToPadding = false
    }

    private fun setViewPagerPaging() {
        val dpValue = 14
        val d: Float = resources.displayMetrics.density
        val margin = (dpValue * d).toInt()
        
        binding.viewpagerBoard.setPadding(margin, 0, margin, 0)
        binding.viewpagerBoard.pageMargin = (margin / 1.7).toInt()
    }

    // board - users에 해당 user uid 등록
    private fun passUserInfoToServer() {
        val userPath = databaseReference.child("board").child(BOARD_CODE).child("users")
        val userModel = UserInfo(UID, TOKEN, USER_NAME, PROFILE_IMG, PUSH_TOKEN)

        userPath.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map: Map<String, *>? = snapshot.value as Map<String, *>?
                val keySet: Set<String>? = map?.keys
                val list: ArrayList<String> = ArrayList()
                if (keySet != null) {
                    list.addAll(keySet)
                } else {
                    userPath.child(UID).setValue(userModel)
                    increaseMemberCount()
                    return
                }

                var increaseFlag = false
                for (i in 0 until list.size) {
                    // board - users에 해당 user의 uid가 있는 경우
                    if (list[i] == UID) {
                        increaseFlag = false
                        break
                    }
                    // 해당 user의 uid가 없거나 users에 아무 항목도 없을 경우
                    else if (list[i] != UID || map.isNullOrEmpty()) {
                        increaseFlag = true
                    }
                }
                if (increaseFlag) {
                    // board - users에 해당 user의 uid column 추가
                    userPath.child(UID).setValue(userModel)
                    increaseMemberCount()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun increaseMemberCount() {
        val boardInfoPath = databaseReference.child("board").child(BOARD_CODE).child("info")
        // board - user에 해당 user의 uid가 없으면 멤버 수 +1 증가
       boardInfoPath.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentCount: Int = (snapshot.child("memberCount").value as Long).toInt()
                        boardInfoPath.child("memberCount").setValue(currentCount + 1)
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
    }

    private fun initIssueButton() {
        // Todo: Issue 뷰와 연결하기
    }

    private fun initCalendarButton() {
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra("boardName", BOARD_NAME)
        intent.putExtra("boardCode", BOARD_CODE)
        startActivity(intent)
    }

    private fun initBackButton() {
        binding.imgbtnBackBoard.setOnClickListener {
            finish()
        }
    }

    fun getCurrentFrag() : Int {
        return when (binding.viewpagerBoard.currentItem) {
            0 -> 0
            1 -> 1
            else -> 2
        }
    }

    fun movePage(index: Int) {
        val handler = Handler()
        handler.postDelayed({
            binding.viewpagerBoard.post {
                binding.viewpagerBoard.setCurrentItem(index, true)
            }
        }, 500)
    }

    fun putTodoTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("todo")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    fun putInProgressTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("inProgress")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    fun putDoneTaskInDatabase(list: MutableList<TaskData>) {
        val recyclerMap = HashMap<String, MutableList<TaskData>>()
        recyclerMap["recyclerArranging"] = list
        databaseReference.child("board").child(BOARD_CODE).child("done")
                .updateChildren(recyclerMap as Map<String, Any>)
    }

    private fun getPushMsgContent(startFrag: Int, finFrag: Int, addPosition: Int, addFlag: Boolean) : String {
        var startFragStr = ""
        var finFragStr = ""
        var addPositionStr = ""

        startFragStr = getPositionStr(startFrag)
        finFragStr = getPositionStr(finFrag)
        addPositionStr = getPositionStr(addPosition)

        return if (addFlag)
        // 카드를 새로 추가했을 경우
            "$USER_NAME added task to $addPositionStr"
        else
        // 카드를 옮겼을 경우
            "$USER_NAME moved task from $startFragStr to $finFragStr"
    }

    private fun getPositionStr(position: Int) : String {
        return when (position) {
            0 -> "To do"
            1 -> "In progress"
            else -> "Done"
        }
    }

    fun sendPushNotification(startFrag: Int, finFrag: Int, addPosition: Int, addFlag: Boolean) {
        if (startFrag != finFrag) {
            val pushMsg = getPushMsgContent(startFrag, finFrag, addPosition, addFlag)
            Toast.makeText(this, pushMsg, Toast.LENGTH_SHORT).show()

            val userPath = databaseReference.child("board").child(BOARD_CODE).child("users")
            // push 알림 보내기
            userPath.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val map: Map<String, *> = snapshot.value as Map<String, *>
                            val keySet: Set<String> = map.keys
                            val list: ArrayList<String> = ArrayList()
                            list.addAll(keySet)

                            for (i in 0 until list.size) {
                                if (list[i] == UID)
                                    continue
                                else if (list[i].isNotEmpty()) {
                                    userPath.child(list[i]).addListenerForSingleValueEvent(object: ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            sendFcm(snapshot.child("pushToken").value.toString(), pushMsg)
                                        }
                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
        }
    }

    private fun sendFcm(pushToken: String, pushMsg: String) {
        val gson = Gson()
        val notificationModel = NotificationModel()

        // background push
        notificationModel.apply {
            to = pushToken
            notification.title = "COLLABOARD"
            notification.text = pushMsg
        }

        // foreground push
        notificationModel.data.apply {
            title = BOARD_NAME
            text = pushMsg
        }

        val requestBody = RequestBody.create("application/json; charset=utf8".toMediaTypeOrNull(),
                gson.toJson(notificationModel))

        val request = Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAwuxUFck:APA91bHuao1MBOGFCeSAhTC2ovYXzyu7JjT_8QevbF1lLB_WRv-e1-iWFqQvRqhgwGW9ewOLQibnvBn5bSyvZipOlic4wvLiNH1mBZeHzcN6lyN95xUQBXyz_UtpUprW7OjSO--6X2xB")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build()

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("okhttptest", e.message.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d("okhttptest", response.message)
            }
        })
    }

    companion object {
        lateinit var mContext: BoardActivity
            private set

        var frag_board_name = ""
        var frag_board_code = ""
    }
}