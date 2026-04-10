package com.woonggon.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale


class CustomAdapter : RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setMemo(memo: Memo) {
        itemView.findViewById<TextView>(R.id.textNo).text = "${memo.no}"
        itemView.findViewById<TextView>(R.id.textTitle).text = memo.title

        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = sdf.format(memo.timestamp)
        itemView.findViewById<TextView>(R.id.textDate).text = date
    }
}

