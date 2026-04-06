package com.woonggon.macdonald_contents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val emailArea = findViewById<EditText>(R.id.loginEmailArea)
        val passwordArea = findViewById<EditText>(R.id.loginPasswordArea)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val goToJoinBtn = findViewById<Button>(R.id.goToJoinBtn)

        loginBtn.setOnClickListener {
            val email = emailArea.text.toString().trim()
            val password = passwordArea.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LoadingDialog.show(this, "로그인중입니다...")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    LoadingDialog.dismiss()
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "signInWithEmail:success")
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        goToJoinBtn.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }
}
