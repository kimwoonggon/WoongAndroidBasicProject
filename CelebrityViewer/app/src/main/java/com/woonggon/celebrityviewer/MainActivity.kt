package com.woonggon.celebrityviewer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "PhotoViewer"
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
        val intent = Intent(this, Bts1Activity::class.java)
        image1.setOnClickListener {
            startActivity(intent)
        }

        // 7개 image들의 이름을 다 붙이고 각각 클릭시 다른 액티비티로 이동!
        val image2 = findViewById<android.widget.ImageView>(R.id.btsImage2)
        val image3 = findViewById<android.widget.ImageView>(R.id.btsImage3)
        val image4 = findViewById<android.widget.ImageView>(R.id.btsImage4)
        val image5 = findViewById<android.widget.ImageView>(R.id.btsImage5)
        val image6 = findViewById<android.widget.ImageView>(R.id.btsImage6)
        val image7 = findViewById<android.widget.ImageView>(R.id.btsImage7)

        image2.setOnClickListener {
            val intent = Intent(this, Bts2Activity::class.java)
            startActivity(intent)
        }

        image3.setOnClickListener {
            val intent = Intent(this, Bts3Activity::class.java)
            startActivity(intent)
        }

        image4.setOnClickListener {
            val intent = Intent(this, Bts4Activity::class.java)
            startActivity(intent)
        }

        image5.setOnClickListener {
            val intent = Intent(this, Bts5Activity::class.java)
            startActivity(intent)
        }

        image6.setOnClickListener {
            val intent = Intent(this, Bts6Activity::class.java)
            startActivity(intent)
        }

        image7.setOnClickListener {
            val intent = Intent(this, Bts7Activity::class.java)
            startActivity(intent)
        }

    }
}