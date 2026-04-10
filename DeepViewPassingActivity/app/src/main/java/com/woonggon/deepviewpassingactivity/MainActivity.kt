package com.woonggon.deepviewpassingactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val REQ_BUTTON = 99
    val REQ_BUTTON2 = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            intent.putExtra("param1", "버튼1에서 신호를 받았습니다")
            startActivityForResult(intent, REQ_BUTTON)
        }

        button2.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            intent.putExtra("param2", "버튼2에서 신호를 받았습니다")
            startActivityForResult(intent, REQ_BUTTON2)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when (requestCode)
            {
                REQ_BUTTON -> {
                    val returnValue = data?.getStringExtra("param2") ?: "None"
                    Log.d("액태비티", "돌려받은 값=$returnValue")
                }
                REQ_BUTTON2 -> {
                    val returnValue = data?.getStringExtra("param2") ?: "None"
                    Log.d("액태비티", "돌려받은 값=$returnValue")
                }
            }

        }
    }
}