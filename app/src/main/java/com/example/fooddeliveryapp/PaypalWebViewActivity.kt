package com.example.fooddeliveryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.utils.PAYMENT_CANCEL
import com.example.fooddeliveryapp.utils.PAYMENT_SUCCESS
import com.example.fooddeliveryapp.utils.URL

class PaypalWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        val url = intent.getStringExtra(URL) ?: ""

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val requestUrl = request?.url.toString()

                if (requestUrl.contains(PAYMENT_SUCCESS)) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(PAYMENT_SUCCESS, true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                    return true
                } else if (requestUrl.contains(PAYMENT_CANCEL)) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(PAYMENT_SUCCESS, false)
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
