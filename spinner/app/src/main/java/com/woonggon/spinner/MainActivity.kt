package com.woonggon.spinner

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data = listOf("- 선택하세요 -", "월",
            "화", "수", "목", "금", "토", "일")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter
        val textView = findViewById<TextView>(R.id.textView)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                val selectedValue = data[p2]
                textView.text = selectedValue

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }
}