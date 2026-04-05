package com.woonggon.wisesaying

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SentenceAdapter(
    private val context: Context,
    private val sentenceList: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = sentenceList.size

    override fun getItem(position: Int): Any = sentenceList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.listview_item, parent, false)

        val sentenceTextView = view.findViewById<TextView>(R.id.sentenceTextView)
        sentenceTextView.text = sentenceList[position]

        return view
    }
}
