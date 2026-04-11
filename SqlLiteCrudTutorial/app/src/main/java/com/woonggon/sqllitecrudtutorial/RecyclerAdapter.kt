package com.woonggon.sqllitecrudtutorial

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat


class RecyclerAdapter : RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<Memo>()
    var onDeleteClick: ((Memo) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)

        return Holder(view)
    }
    // 화면에 실제로 그려주는 함수임
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
        holder.itemView.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("메모 삭제")
                .setMessage("이 메모를 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    onDeleteClick?.invoke(memo)
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }


}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textNo: TextView = itemView.findViewById(R.id.textNo)
    private val textContent: TextView = itemView.findViewById(R.id.textContent)
    private val textDatetime: TextView = itemView.findViewById(R.id.textDatetime)

    fun setMemo(memo: Memo)
    {
        textNo.text = "${memo.no}"
        textContent.text = "${memo.content}"
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
        val datetime = sdf.format(memo.datetime)
        textDatetime.text = "$datetime"


    }

}