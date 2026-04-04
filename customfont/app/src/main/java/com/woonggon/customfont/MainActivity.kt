package com.woonggon.customfont

import android.os.Bundle
import android.util.Log
import android.util.Log.*
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

        var testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")
        // List에 toString은 [a,b,c]와 같이 list가 아닌 string으로 바꿔 준다
        d("MainActivity", testList.toString())
    }

}