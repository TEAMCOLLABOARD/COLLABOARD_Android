package com.example.collaboard_android.board.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R
import com.example.collaboard_android.board.adapter.TaskAdapter
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.TaskListener
import com.example.collaboard_android.databinding.FragmentTodoBinding

class TodoFragment : Fragment(), TaskListener {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerList: MutableList<TaskData>

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
    }

    private fun initRecyclerView() {
        val list: MutableList<TaskData> = mutableListOf(
                TaskData("Feature", "D-1", "실습 프로젝트 내용 옮겨오기",
                        ResourcesCompat.getDrawable(activity!!.resources, R.drawable.image_profile, null), "heewon"),
                TaskData("Refactor", "D-1", "FilterBottomSheetFragment.kt, ListActivity.kt 리팩토링 하기 FilterBottomSheetFragment.kt, ListActivity.kt 리팩토링 하기",
                        ResourcesCompat.getDrawable(activity!!.resources, R.drawable.image_profile, null), "heewon"),
                TaskData("Fix", "D-1", "디자인 수정",
                        ResourcesCompat.getDrawable(activity!!.resources, R.drawable.image_profile, null), "heewon"),
                TaskData("Chore", "D-1", "라이브러리 추가",
                        ResourcesCompat.getDrawable(activity!!.resources, R.drawable.image_profile, null), "heewon")
        )

        binding.recyclerviewTodo?.init(list, binding.tvEmptyTodo)
    }

    private fun RecyclerView.init(list: MutableList<TaskData>, emptyTextView: TextView) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = TaskAdapter(list, this@TodoFragment)
        this.adapter = adapter

        recyclerList = list

        // recyclerview dataset 바뀔 때마다 notifyDataSetChanged()
        //adapter.notifyDataSetChanged()

        //emptyTextView.setOnDragListener(adapter.dragInstance)
        //this.setOnDragListener(adapter.dragInstance)
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