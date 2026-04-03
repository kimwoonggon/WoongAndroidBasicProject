package com.woonggon.twice

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// image view load
// detailimage
import android.content.Intent
import android.widget.Toast
// setimageresource


class ImageInsideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_inside)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageResId = intent.getIntExtra("imageResId", R.drawable.member_1)
        findViewById<ImageView>(R.id.detailImage).setImageResource(imageResId)

        Toast.makeText(this, "Image Resource ID: $imageResId", Toast.LENGTH_SHORT).show()
    }
}