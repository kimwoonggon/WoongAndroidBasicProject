package com.woonggon.camerabasic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    private val FLAG_PERM_CAMERA = 98
    private val FLAG_REQ_CAMERA = 101

    private lateinit var buttonCamera: Button
    private lateinit var imagePreview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonCamera = findViewById(R.id.buttonCamera)
        imagePreview = findViewById(R.id.imagePreview)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonCamera.setOnClickListener {
            if (isPermitted(CAMERA_PERMISSION)) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, CAMERA_PERMISSION, FLAG_PERM_CAMERA)
            }
        }
    }

    private fun isPermitted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, FLAG_REQ_CAMERA)
    }

    @Deprecated("API 30+ 는 onActivityResult이 비권장입니다. 기능 동작 확인용으로 우선 기존 방식 유지.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FLAG_REQ_CAMERA && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                imagePreview.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "카메라 이미지가 전달되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FLAG_PERM_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                openCamera()
            } else {
                Toast.makeText(this, "카메라 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
