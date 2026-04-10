package com.woonggon.deepviewpassingactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView2 = findViewById<TextView>(R.id.textView2)
        val btnResult = findViewById<Button>(R.id.btnResult)

        val param = intent.getStringExtra("param1")
            ?: intent.getStringExtra("param2")
            ?: "NoSigNAL"
        textView2.text = param

        btnResult.setOnClickListener {
            val intent = Intent()
            intent.putExtra("param2", "돌려드립니다")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}