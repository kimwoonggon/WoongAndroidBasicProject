package com.woonggon.macdonald_contents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RVAdapter(val context: Context, val List: MutableList<ContentsModel>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    interface ItemClick {
        fun onCLick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.rv_item, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(List[position])
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick!!.onCLick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: ContentsModel) {
            val rv_img = itemView.findViewById<ImageView>(R.id.rvImageArea)
            val rv_text = itemView.findViewById<TextView>(R.id.rvTextArea)

            rv_text.text = item.titleText
            Glide.with(context).load(item.ImageUrl).into(rv_img)
        }
    }
}