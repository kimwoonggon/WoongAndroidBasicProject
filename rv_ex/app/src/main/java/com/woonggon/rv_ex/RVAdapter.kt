package com.woonggon.rv_ex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ItemClick {
    fun onClick(view: View, position: Int)
}

class RVAdapter(val items: MutableList<String>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): RVAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.rv_item, p0, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(p0: RVAdapter.ViewHolder, p1: Int) {
        if (itemClick != null)
        {
            p0.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, p1)
            }
        }
        p0.bindItems(items[p1])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item : String) {
            val rv_text = itemView.findViewById<android.widget.TextView>(R.id.rvItem)
            rv_text.text = item
        }
    }
}