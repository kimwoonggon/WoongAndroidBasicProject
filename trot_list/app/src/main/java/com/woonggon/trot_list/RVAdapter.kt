package com.woonggon.trot_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(val items: MutableList<String>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): RVAdapter.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.rv_item, p0, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(p0: RVAdapter.ViewHolder, p1: Int) {
        p0.bindItems(items[p1])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(item: String) {
            val rv_text = itemView.findViewById<TextView>(R.id.rvTextId)
            rv_text.text = item
        }


    }



}