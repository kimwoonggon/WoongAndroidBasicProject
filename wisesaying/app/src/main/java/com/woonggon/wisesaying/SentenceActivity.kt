package com.woonggon.wisesaying

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SentenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sentence)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sentenceList = intent.getStringArrayListExtra("sentenceList") ?: arrayListOf()
        val listView = findViewById<ListView>(R.id.sentenceListView)
        listView.adapter = SentenceAdapter(this, sentenceList)
    
        listView.setOnItemClickListener { parent, view, position, id ->
            // 아이템 클릭 시 동작할 코드 작성
            val selectedSentence = sentenceList[position]
            // 예: 토스트 메시지로 선택된 문장 표시
            Toast.makeText(this, selectedSentence, Toast.LENGTH_SHORT).show()
        }
    }


}