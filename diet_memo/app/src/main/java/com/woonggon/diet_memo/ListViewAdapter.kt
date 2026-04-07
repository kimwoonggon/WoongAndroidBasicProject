package com.woonggon.diet_memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val List : MutableList<DataModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(p0: Int): Any? {
        return List[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(
        position: Int,
        inputView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = inputView
        if (convertView == null)
        {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }
        val date = convertView!!.findViewById<TextView>(R.id.listViewDateArea)
        val memo = convertView!!.findViewById<TextView>(R.id.listViewMemoArea)

        date.text = List[position].date
        memo.text = List[position].memo

        return convertView!!
    }

}