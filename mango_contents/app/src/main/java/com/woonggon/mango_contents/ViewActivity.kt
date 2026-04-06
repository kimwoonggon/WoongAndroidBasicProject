package com.woonggon.mango_contents

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// 웹뷰
class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //intent.getStringExtra("url")

        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        // webViewClient = WebViewClient() — 이게 없으면 링크 클릭 시 외부 브라우저로 넘어가고, 페이지 내 리다이렉트/동적 네비게이션을 WebView 안에서 처리 못합니다.
        // javaScriptEnabled = true — 맥도날드 사이트처럼 JS로 동적 렌더링하는 페이지는 이게 없으면 빈 화면이 나옵니다.
        // domStorageEnabled = true — 많은 웹사이트가 localStorage/sessionStorage를 사용하므로 이것도 켜야 정상 동작합니다.
        webView.settings.domStorageEnabled = true
        webView.loadUrl(intent.getStringExtra("url").toString())

    }
}