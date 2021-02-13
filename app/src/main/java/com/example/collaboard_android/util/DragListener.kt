package com.example.collaboard_android.util

import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R
import com.example.collaboard_android.board.adapter.TaskAdapter
import com.example.collaboard_android.board.adapter.TaskData
import com.example.collaboard_android.board.adapter.TaskListener
import com.example.collaboard_android.board.ui.BoardActivity
import com.example.collaboard_android.board.ui.BoardActivity.Companion.mContext

class DragListener internal constructor(private val listener: TaskListener) : View.OnDragListener {

    private var isDropped = false

    private var distance = 0.0F
    private var pressX = 0.0F
    var flag = false

    var startFrag = 0
    var finFrag = 0

    override fun onDrag(v: View, event: DragEvent): Boolean {

        val aContext: BoardActivity = mContext as BoardActivity

        when (event.action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                pressX = event.x

                startFrag = when {
                    aContext.getCurrentFrag() == 0 -> 0
                    aContext.getCurrentFrag() == 1 -> 1
                    aContext.getCurrentFrag() == 2 -> 2
                    else -> -1
                }
            }

            DragEvent.ACTION_DRAG_LOCATION, DragEvent.ACTION_DRAG_ENTERED -> {
                distance = pressX - event.x
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                if (kotlin.math.abs(distance) < 50)
                    return false
                else {
                    // TodoFragment
                    if (distance > 0 && aContext.getCurrentFrag() == 0) {
                        flag = true
                        return false
                    }
                    if (distance < 0 && aContext.getCurrentFrag() == 0) {
                        aContext.movePage(1)
                        flag = true
                        return true
                    }

                    // InProgressFragment
                    if (distance > 0 && aContext.getCurrentFrag() == 1 && !flag) {
                        aContext.movePage(0)
                        flag = false
                    }
                    if (distance < 0 && aContext.getCurrentFrag() == 1 && !flag) {
                        aContext.movePage(2)
                        flag = false
                    }

                    // DoneFragment
                    if (distance > 0 && aContext.getCurrentFrag() == 2) {
                        aContext.movePage(1)
                        flag = true
                        return true
                    }
                    if (distance < 0 && aContext.getCurrentFrag() == 2) {
                        flag = true
                        return false
                    }
                }
            }

            DragEvent.ACTION_DROP -> {
                isDropped = true

                var positionTarget = -1
                val viewSource = event.localState as View?
                val viewId = v.id
                val constraintItem = R.id.constraintLayout_task

                val emptyTextTodo = R.id.tv_empty_todo
                val emptyTextInProgress = R.id.tv_empty_in_progress
                val emptyTextDone = R.id.tv_empty_done

                val recyclerTodo = R.id.recyclerview_todo
                val recyclerInProgress = R.id.recyclerview_in_progress
                val recyclerDone = R.id.recyclerview_done

                when (viewId) {
                    constraintItem, emptyTextTodo, emptyTextInProgress, emptyTextDone,
                    recyclerTodo, recyclerInProgress, recyclerDone -> {

                        val target: RecyclerView

                        when (viewId) {
                            emptyTextTodo, recyclerTodo -> target =
                                    v.rootView.findViewById<View>(recyclerTodo) as RecyclerView
                            emptyTextInProgress, recyclerInProgress -> target =
                                    v.rootView.findViewById<View>(recyclerInProgress) as RecyclerView
                            emptyTextDone, recyclerDone -> target =
                                    v.rootView.findViewById<View>(recyclerDone) as RecyclerView
                            else -> {
                                target = v.parent as RecyclerView
                                positionTarget = v.tag as Int
                            }
                        }

                        if (viewSource != null) {
                            val source = viewSource.parent as RecyclerView
                            val adapterSource = source.adapter as TaskAdapter?
                            val positionSource = viewSource.tag as Int
                            val list: TaskData? = adapterSource?.getList()?.get(positionSource)
                            val listSource = adapterSource?.getList()?.apply {
                                removeAt(positionSource)
                            }

                            listSource?.let { adapterSource.updateList(it) }
                            adapterSource?.notifyDataSetChanged()

                            val adapterTarget = target.adapter as TaskAdapter?
                            val customListTarget = adapterTarget?.getList()

                            if (positionTarget >= 0)
                                list?.let { customListTarget?.add(positionTarget, it) }
                            else
                                list?.let { customListTarget?.add(it) }

                            customListTarget?.let { adapterTarget.updateList(it) }
                            adapterTarget?.notifyDataSetChanged()

                            if (source.id == recyclerInProgress && adapterSource?.itemCount ?: 0 < 1) {
                                listener.setEmptyList(View.VISIBLE, recyclerInProgress, emptyTextInProgress)
                            }
                            if (viewId == emptyTextInProgress) {
                                listener.setEmptyList(View.GONE, recyclerInProgress, emptyTextInProgress)
                                v.rootView.findViewById<View>(recyclerInProgress).visibility = View.VISIBLE
                            }

                            if (source.id == recyclerTodo && adapterSource?.itemCount ?: 0 < 1) {
                                listener.setEmptyList(View.VISIBLE, recyclerTodo, emptyTextTodo)
                            }
                            if (viewId == emptyTextTodo) {
                                listener.setEmptyList(View.GONE, recyclerTodo, emptyTextTodo)
                                v.rootView.findViewById<View>(recyclerTodo).visibility = View.VISIBLE
                            }

                            if (source.id == recyclerDone && adapterSource?.itemCount ?: 0 < 1) {
                                listener.setEmptyList(View.VISIBLE, recyclerDone, emptyTextDone)
                            }
                            if (viewId == emptyTextDone) {
                                listener.setEmptyList(View.GONE, recyclerDone, emptyTextDone)
                                v.rootView.findViewById<View>(recyclerDone).visibility = View.VISIBLE
                            }

                        }
                    }
                }

                finFrag = when {
                    aContext.getCurrentFrag() == 0 -> 0
                    aContext.getCurrentFrag() == 1 -> 1
                    aContext.getCurrentFrag() == 2 -> 2
                    else -> -1
                }

                // 서버로 바뀐 recyclerview data set 업로드

            }
        }

        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }
}