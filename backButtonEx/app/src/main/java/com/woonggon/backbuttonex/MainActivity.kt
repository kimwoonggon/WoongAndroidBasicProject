package com.woonggon.backbuttonex

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    
    private var isDouble = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 그냥 handleonbackProessed는 deprecated 되었지만, onBackPressedDispatcher는 deprecated되지 않았습니다.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("MainActivity", "backbutton")
                if (isDouble) {
                    finish()
                }
                isDouble = true
                Toast.makeText(this@MainActivity, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                // Handler는 일정 시간 후에 특정 작업을 실행할 수 있도록 도와주는 클래스입니다. 여기서는 2초 후에 isDouble 변수를 false로 설정하는 작업을 예약합니다.
                Handler(Looper.getMainLooper()).postDelayed({
                    isDouble = false
                }, 2000)
            }
        })
    }

}