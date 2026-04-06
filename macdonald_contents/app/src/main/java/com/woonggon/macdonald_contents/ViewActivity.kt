package com.woonggon.macdonald_contents

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// 웹뷰
class ViewActivity : AppCompatActivity() {

    private var savedKey: String? = null // 저장된 항목의 key (null이면 미저장)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val url = intent.getStringExtra("url").toString()
        val title = intent.getStringExtra("title").toString()
        val imageUrl = intent.getStringExtra("imageUrl").toString()


        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(url)

        val saveBtn = findViewById<TextView>(R.id.saveBtn)
        val uid = Firebase.auth.currentUser?.uid

        // 진입 시 이미 저장된 URL인지 확인
        if (uid != null) {
            val bookmarksRef = Firebase.database.reference.child("bookmarks").child(uid)
            bookmarksRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val bookmark = child.getValue(ContentsModel::class.java)
                        if (bookmark?.url == url && bookmark?.ImageUrl == imageUrl && bookmark?.titleText == title) {
                            savedKey = child.key
                            saveBtn.text = "해제"
                            return
                        }
                    }
                    savedKey = null
                    saveBtn.text = "저장"
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        saveBtn.setOnClickListener {
            if (uid == null) {
                Toast.makeText(this, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bookmarksRef = Firebase.database.reference.child("bookmarks").child(uid)

            if (savedKey != null) {
                // 이미 저장됨 → 해제
                bookmarksRef.child(savedKey!!).removeValue()
                    .addOnSuccessListener {
                        savedKey = null
                        saveBtn.text = "저장"
                        Toast.makeText(this, "해제 완료", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "해제 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // 미저장 → 저장
                val key = bookmarksRef.push().key
                if (key != null) {
                    bookmarksRef.child(key).setValue(ContentsModel(url, title, imageUrl))
                        .addOnSuccessListener {
                            savedKey = key
                            saveBtn.text = "해제"
                            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}