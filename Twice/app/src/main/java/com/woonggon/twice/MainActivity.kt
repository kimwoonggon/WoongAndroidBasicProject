package com.woonggon.twice

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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

        fun openImage(imageResId: Int) {
            val intent = Intent(this, ImageInsideActivity::class.java)
            intent.putExtra("imageResId", imageResId)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.member1).setOnClickListener { openImage(R.drawable.member_1) }
        findViewById<ImageView>(R.id.member2).setOnClickListener { openImage(R.drawable.member_2) }
        findViewById<ImageView>(R.id.member3).setOnClickListener { openImage(R.drawable.member_3) }
        findViewById<ImageView>(R.id.member4).setOnClickListener { openImage(R.drawable.member_4) }
        findViewById<ImageView>(R.id.member5).setOnClickListener { openImage(R.drawable.member_5) }
        findViewById<ImageView>(R.id.member6).setOnClickListener { openImage(R.drawable.member_6) }
        findViewById<ImageView>(R.id.member7).setOnClickListener { openImage(R.drawable.member_7) }
        findViewById<ImageView>(R.id.member8).setOnClickListener { openImage(R.drawable.member_8) }
        findViewById<ImageView>(R.id.member9).setOnClickListener { openImage(R.drawable.member_9) }

    }
}