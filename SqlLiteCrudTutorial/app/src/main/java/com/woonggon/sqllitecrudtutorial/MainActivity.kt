package com.woonggon.sqllitecrudtutorial

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val DB_NAME = "sqlite.sql"
    val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val helper = SqliteHelper(this, DB_NAME, DB_VERSION)
        val memos = helper.selectMemo()

        val recyclerMemo = findViewById<RecyclerView>(R.id.recyclerMemo)
        val adapter = RecyclerAdapter()
        adapter.listData.addAll(memos)
        adapter.onDeleteClick = { memo ->
            helper.deleteMemo(memo)
            adapter.listData.clear()
            adapter.listData.addAll(helper.selectMemo())
            adapter.notifyDataSetChanged()
        }
        recyclerMemo.adapter = adapter
        recyclerMemo.layoutManager = LinearLayoutManager(this)

        val editMemo = findViewById<EditText>(R.id.editMemo)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val content = editMemo.text.toString()
            if (content.isNotEmpty()) {
                val memo = Memo(0, content, System.currentTimeMillis())
                helper.insertMemo(memo)

                adapter.listData.clear()
                adapter.listData.addAll(helper.selectMemo())
                adapter.notifyDataSetChanged()

                editMemo.setText("")
            }
        }

    }
}