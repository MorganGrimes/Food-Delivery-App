package com.example.fooddeliveryapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class PaypalWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        val url = intent.getStringExtra("url") ?: ""

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()

                if (url.contains("payment_success")) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("paymentSuccess", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                    return true
                } else if (url.contains("payment_cancel")) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("paymentSuccess", false)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                    return true
                }
                return false
            }
        }
        webView.loadUrl(url)
    }
}
