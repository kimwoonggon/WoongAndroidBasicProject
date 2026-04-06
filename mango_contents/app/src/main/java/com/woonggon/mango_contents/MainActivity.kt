package com.woonggon.mango_contents

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
import androidx.recyclerview.widget.RecyclerView

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
        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))
        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))
        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

        items.add(ContentsModel("https://www.mcdonalds.co.kr/kor/menu/burger",
            "https://www.mcdonalds.co.kr/upload/2026/03/Corp_PC_VIEW_772x530_%EB%B2%A0%ED%86%A0%EB%94%94%EB%B0%94%EC%A7%88%ED%81%AC%EB%A6%BC_EVM_2.png",
            "베토디바질크림치즈세트"))

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
                startActivity(intent)
            }
        }
    }
}