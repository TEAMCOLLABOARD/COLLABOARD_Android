package com.example.collaboard_android.board.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.board.adapter.TaskAdapter
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.TaskListener
import com.example.collaboard_android.board.ui.BoardActivity.Companion.frag_board_code
import com.example.collaboard_android.R
import com.example.collaboard_android.util.calDeadline
import com.example.collaboard_android.util.getDeadlineString
import com.example.collaboard_android.util.getLabelString
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_in_progress.*

class InProgressFragment : Fragment(), TaskListener {

    private lateinit var recyclerList: MutableList<TaskData>
    private lateinit var taskAdapter: TaskAdapter

    var list: MutableList<TaskData> = mutableListOf()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    val boardContext: BoardActivity = BoardActivity.mContext

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_empty_in_progress.visibility = View.GONE

        initRecyclerView()

        itemTouchHelper.attachToRecyclerView(recyclerview_in_progress)

        initAddButton()
    }

    private fun initAddButton() {
        btn_add.setOnClickListener {
            val addTaskDialog = AddTaskDialogFragment { description: String, label: Int, pickDate: IntArray ->
                val labelString = getLabelString(label)
                val deadline = calDeadline(pickDate)
                val deadlineString = getDeadlineString(deadline)

                addRecyclerItemToAdapter(labelString, deadlineString, description)
            }
            addTaskDialog.show(childFragmentManager, "add_task_dialog")
        }
    }

    private fun addRecyclerItemToAdapter(label: String, deadline: String, description: String) {
        list.clear()
        recyclerview_in_progress?.init(list, tv_empty_in_progress)

        //Todo: 더미로 넣어놓은 프로필 이미지, 사용자 이름 수정하기
        val taskData = TaskData(label, deadline, description,
                "https://avatars.githubusercontent.com/u/52772787?s=460&u=4a9f12ef174f88ec143b70f4fcaaa8f1b2d87b43&v=4", "heewon")

        taskAdapter.addItem(taskData)
        taskAdapter.notifyDataSetChanged()

        //recyclerList.add(taskData)

        boardContext.putInProgressTaskInDatabase(taskAdapter.getList())
    }

    private fun initRecyclerView() {
        //Todo: 서버에서 가져온 초기 데이터 뿌려주기
        databaseReference.child("board").child(frag_board_code).child("inProgress")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    list = mutableListOf()

                    val map: ArrayList<*>? = snapshot.child("recyclerArranging").value as ArrayList<*>?

                    if (map.isNullOrEmpty()) {
                        recyclerview_in_progress?.init(list, tv_empty_in_progress)
                        return
                    }

                    for (i in 0 until map.size) {
                        val hashMap: HashMap<String, String>? = map[i] as HashMap<String, String>?
                        val taskData = TaskData(
                            hashMap?.get("label").toString(),
                            hashMap?.get("deadline").toString(),
                            hashMap?.get("content").toString(),
                            hashMap?.get("profileImg").toString(),
                            hashMap?.get("userName").toString()
                        )
                        list.add(taskData)
                    }
                    recyclerview_in_progress?.init(list, tv_empty_in_progress)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun RecyclerView.init(list: MutableList<TaskData>?, emptyTextView: TextView) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (!list.isNullOrEmpty()) {
            taskAdapter = TaskAdapter(list, this@InProgressFragment)
            this.adapter = taskAdapter

            recyclerList = list
        }
        else {
            val mList = mutableListOf<TaskData>()
            taskAdapter = TaskAdapter(mList, this@InProgressFragment)
            this.adapter = taskAdapter

            recyclerList = mList
        }

        // recyclerview dataset 바뀔 때마다 notifyDataSetChanged()
        taskAdapter.notifyDataSetChanged()

        emptyTextView.setOnDragListener(taskAdapter.dragInstance)
        this.setOnDragListener(taskAdapter.dragInstance)
    }

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
                object : ItemTouchHelper.SimpleCallback (ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.START or ItemTouchHelper.END, 0) {
                    override fun onSelectedChanged(
                            viewHolder: RecyclerView.ViewHolder?,
                            actionState: Int
                    ) {
                        super.onSelectedChanged(viewHolder, actionState)

                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            viewHolder?.itemView?.alpha = 0.5f
                        }
                    }

                    override fun clearView(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                    ) {
                        super.clearView(recyclerView, viewHolder)
                        viewHolder.itemView.alpha = 1.0f

                        val adapter = recyclerView.adapter as TaskAdapter
                        recyclerList = adapter.getList()

                        boardContext.putInProgressTaskInDatabase(recyclerList)
                    }

                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                    ): Boolean {
                        val adapter = recyclerView.adapter as TaskAdapter
                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition

                        adapter.moveItem(from, to)
                        adapter.notifyItemMoved(from, to)

                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
                }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun setEmptyList(visibility: Int, recyclerView: Int, emptyTextView: Int) {
        recyclerview_in_progress.visibility = visibility
        tv_empty_in_progress.visibility = visibility
    }
}