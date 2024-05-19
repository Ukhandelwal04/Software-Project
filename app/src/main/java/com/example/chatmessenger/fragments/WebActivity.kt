package com.example.chatmessenger.fragments

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.MainActivity
import com.example.chatmessenger.R
import com.example.chatmessenger.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var pgBar: ProgressBar
    private lateinit var chaa: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webl)

        // Find WebView and ProgressBar by their IDs
        webView = findViewById(R.id.webView)
        chaa = findViewById(R.id.cha)
        pgBar = findViewById(R.id.pgBar)

        webView.settings.javaScriptEnabled = true

        // Setup WebView
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show progress bar when page starts loading
                pgBar.visibility = ProgressBar.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide progress bar when page finishes loading
                pgBar.visibility = ProgressBar.GONE
            }
        }

        // Load initial URL
        webView.loadUrl("http://192.168.244.250:3000/index.html")



        chaa.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onBackPressed() {
        // Check if webView can go back
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
