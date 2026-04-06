package com.woonggon.macdonald_contents

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BookMarkActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private val contentModels = mutableListOf<ContentsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_mark)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val recyclerView = findViewById<RecyclerView>(R.id.bookmarkRv)
        val rvAdapter = RVAdapter(this, contentModels)
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        val database = Firebase.database
        val myBookmarkRef = database.getReference("bookmarks")

        LoadingDialog.show(this, "북마크를 불러오는중입니다...")

        myBookmarkRef
            .child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    LoadingDialog.dismiss()
                    contentModels.clear()
                    for (dataModel in p0.children) {
                        val item = dataModel.getValue(ContentsModel::class.java)
                        if (item != null) {
                            contentModels.add(item)
                        }
                    }
                    rvAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(p0: DatabaseError) {
                    LoadingDialog.dismiss()
                }

            })

    }
}