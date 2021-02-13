package com.example.collaboard_android.board.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

interface TaskListener {
    fun setEmptyList(visibility: Int, recyclerView: Int, emptyTextView: Int)
}

class TaskAdapter(private var list: MutableList<TaskData>, private val listener: TaskListener?)
    : RecyclerView.Adapter<TaskViewHolder>(), View.OnTouchListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: MutableList<TaskData>) {
        this.list = list
    }

    fun getList() : MutableList<TaskData> = this.list.toMutableList()

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v?.startDrag(data, shadowBuilder, v, 0)

                return true
            }
        }
        return false
    }

    /*val dragInstance: DragListener?
    get() = if (listener != null) {
        DragListener(listener)
    } else {
        Log.e(javaClass::class.simpleName, "Listener not initialized")
        null
    }*/

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.constraintLayout.tag = position
        holder.constraintLayout.setOnTouchListener(this)
        //holder.constraintLayout.setOnDragListener(DragListener(listener!!))
    }

    fun moveItem(from: Int, to: Int) {
        if (from < to) {
            for (i in from until to) {
                Collections.swap(list, i, i+1)
            }
        }
        else {
            for (i in from downTo to+1) {
                Collections.swap(list, i, i-1)
            }
        }
    }

}