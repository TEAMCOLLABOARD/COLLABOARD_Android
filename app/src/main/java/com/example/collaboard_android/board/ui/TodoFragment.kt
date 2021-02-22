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
import com.example.collaboard_android.databinding.FragmentTodoBinding
import com.example.collaboard_android.util.calDeadline
import com.example.collaboard_android.util.getDeadlineString
import com.example.collaboard_android.util.getLabelString
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class TodoFragment : Fragment(), TaskListener {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerList: MutableList<TaskData>
    private lateinit var taskAdapter: TaskAdapter

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    val boardContext: BoardActivity = BoardActivity.mContext

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvEmptyTodo.visibility = View.GONE

        itemTouchHelper.attachToRecyclerView(binding.recyclerviewTodo)

        initRecyclerView()

        initAddButton()
    }

    private fun initAddButton() {
        binding.btnAdd.setOnClickListener {
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
        //Todo: 더미로 넣어놓은 프로필 이미지, 사용자 이름 수정하기
        val taskData = TaskData(label, deadline, description,
                "https://avatars.githubusercontent.com/u/52772787?s=460&u=4a9f12ef174f88ec143b70f4fcaaa8f1b2d87b43&v=4", "heewon")

        taskAdapter.addItem(taskData)
        taskAdapter.notifyDataSetChanged()

        //recyclerList.add(taskData)

        boardContext.putTodoTaskInDatabase(taskAdapter.getList())
    }

    private fun initRecyclerView() {
        //Todo: 서버에서 가져온 초기 데이터 뿌려주기
        val list: MutableList<TaskData> =  mutableListOf()
        binding.recyclerviewTodo?.init(list, binding.tvEmptyTodo)
    }

    private fun RecyclerView.init(list: MutableList<TaskData>, emptyTextView: TextView) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskAdapter = TaskAdapter(list, this@TodoFragment)
        this.adapter = taskAdapter

        recyclerList = list

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

                    boardContext.putTodoTaskInDatabase(recyclerList)
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

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }

            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun setEmptyList(visibility: Int, recyclerView: Int, emptyTextView: Int) {
        binding.recyclerviewTodo.visibility = visibility
        binding.tvEmptyTodo.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}