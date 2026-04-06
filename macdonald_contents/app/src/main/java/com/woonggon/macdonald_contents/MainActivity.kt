package com.woonggon.macdonald_contents

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private val items = mutableListOf<ContentsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bookmarkButton = findViewById<TextView>(R.id.bookmarkBtn)
        bookmarkButton.setOnClickListener {
            val intent = Intent(this, BookMarkActivity::class.java)
            startActivity(intent)

        }


        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%A7%A5%EC%8A%A4%ED%8C%8C%EC%9D%B4%EC%8B%9C%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "맥스파이시바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/02/Corp_PC_VIEW_772x530_%EB%8D%94%EB%B8%94%EC%83%81%ED%95%98%EC%9D%B4_EVM.png",
            "빅맥세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/product/pcfile/1723564262197.png",
            "더블스파이시상하이버거세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/product/pcfile/1723562660091.png",
            "맥스파이시상하이버거세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/product/pcfile/1723564262197.png",
            "1955버거세트"))
        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/product/pcfile/1723563759418.png",
            "더블 쿼터파운더 치즈 세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/product/pcfile/1723563812629.png",
            "쿼터파운더 치즈세트"))
        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/02/Corp_PC_VIEW_%EB%A7%A5%ED%81%AC%EB%A6%AC%ED%94%BC_%EC%B9%98%ED%82%A8_%EB%94%94%EB%9F%AD%EC%8A%A4_772x530_new.png",
            "맥크리스피 치킨 디럭스 세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/02/Corp_PC_VIEW_%EB%A7%A5%ED%81%AC%EB%A6%AC%ED%94%BC_%EC%B9%98%ED%82%A8_%ED%81%B4%EB%9E%98%EC%8B%9D_772x530_new.png",
            "맥크리스피 치킨클래식 세트"))

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        // 그냥 세로로
        //recyclerView.layoutManager = LinearLayoutManager(this)
        // 두줄로 하기
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val rvAdapter = RVAdapter(baseContext, items)
        recyclerView.adapter = rvAdapter

        // 클릭 연동
        rvAdapter.itemClick = object : RVAdapter.ItemClick {
            override fun onCLick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra("url", "https://www.mcdonalds.co.kr/kor/menu/detail/798/2/16?exposure=recommend")
                intent.putExtra("title", items[position].titleText)
                intent.putExtra("imageUrl", items[position].imageUrl)
                startActivity(intent)
            }
        }

        // 로그아웃
        val logoutBtn = findViewById<TextView>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}