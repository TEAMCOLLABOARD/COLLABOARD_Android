package com.example.collaboard_android.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collaboard_android.R
import com.example.collaboard_android.util.getLabelString

class DeadlineAdapter(
    private val context: Context,
    private val deadlineList: ArrayList<DeadlineDTO>
) :
    RecyclerView.Adapter<DeadlineAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val content = itemView?.findViewById<TextView>(R.id.tv_calendar_content)
        private val label = itemView?.findViewById<TextView>(R.id.tv_label)

        fun bind(list: DeadlineDTO) {
            // TextView와 String 데이터 연결
            content?.text = list.content
            label?.text = getLabelString(list.label)
        }
    }

    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml파일을 inflate하여 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_deadline, parent, false)
        return Holder(view)
    }

    // RecyclerView로 만들어지는 item의 총 개수를 반환
    override fun getItemCount(): Int {
        return deadlineList.size
    }

    // 위의 onCreateViewHolder에서 만든 view와 실제 입력되는 각각의 데이터를 연결
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(deadlineList[position])
    }
}