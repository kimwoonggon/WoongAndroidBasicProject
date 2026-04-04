package com.woonggon.vallog

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

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

        Log.d("MainActivity", "testLog")

        val test = " 여기는 테스트입니다 ㅋㅋ"
        // adb logcat -s MainActivity
        Log.d("MainActivity", test)
        // Log 종료는?
        Log.e("MainActivity", test) // 오류
        Log.w("MainActivity", test) // 경고
        Log.i("MainActivity", test) // 정보
        Log.d("MainActivity", test) // 디버그
        Log.v("MainActivity", test) // 상세
    }
}