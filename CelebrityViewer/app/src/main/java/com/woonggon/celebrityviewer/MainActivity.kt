package com.woonggon.celebrityviewer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

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

        // 1. 화면이 클릭되었다는 것을 인지해야 한다.
        val image1 = findViewById<android.widget.ImageView>(R.id.btsImage1)
        // Toast.LENGTH_SHORT : 토스트 메시지가 짧은 시간동안 화면에 나타나도록 설정하는 상수입니다. 일반적으로 2초 정도 지속됩니다.
        image1.setOnClickListener {
            Toast.makeText(this, "1번 클릭 완료", Toast.LENGTH_SHORT).show()
        }

        // 2. 화면이 클릭되면 다음 화면으로 넘어가서, 사진을 크게 보여준다.
                
    }
}