package com.example.collaboard_android.board.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R
import com.example.collaboard_android.board.adapter.TaskAdapter
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.TaskListener
import com.example.collaboard_android.databinding.FragmentTodoBinding
import com.example.collaboard_android.util.calDeadline
import com.example.collaboard_android.util.getDeadlineString
import com.example.collaboard_android.util.getLabelString
import java.util.*

class TodoFragment : Fragment(), TaskListener {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerList: MutableList<TaskData>
    private lateinit var taskAdapter: TaskAdapter

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
        val taskData = TaskData(label, deadline, description,
                ResourcesCompat.getDrawable(activity!!.resources, R.drawable.image_profile, null), "heewon")

        taskAdapter.addItem(taskData)
        taskAdapter.notifyDataSetChanged()

        //recyclerList.add(taskData)
    }

    private fun initRecyclerView() {
        // 서버에서 리스트 받아오기
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

                    //val aContext: BoardActivity = BoardActivity.mContext as BoardActivity
                    //aContext.putFirstRecyclerInDatabase(recyclerList)
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