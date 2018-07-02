package com.example.dys.testpay

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test_pay.*

class TestPayActivity : Activity() {
    companion object {
        const val TAG = "TestPayActivity>>>"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_pay)
        btn_back.setOnClickListener { finish() }

        webview.settings.apply {
            javaScriptEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            allowFileAccess = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
        }
        webview.webChromeClient = WebChromeClient()
        var num = 0 //控制wxUrl加载的次数
        var platformapiUrl = "" //记录支付宝startApp的url信息，用以点击“继续支付”时再次唤起
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, reloadUrl: String?): Boolean {
                Log.e(TAG, "reloadUrl======$reloadUrl")
//*******************test wx pay*************************************************************
                //唤起微信app
                if (reloadUrl!!.startsWith("weixin://wap/pay")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reloadUrl))
                    startActivity(intent)
                }
                //微信支付信息url加上Referer并重新加载获取唤起url
                if (reloadUrl.startsWith("https://wx.tenpay.com")) {
                    if (("4.4.3" == android.os.Build.VERSION.RELEASE) ||
                            ("4.4.4" == android.os.Build.VERSION.RELEASE)) {
                        //兼容这两个版本设置referer无效的问题
                        if (num < 1) {
                            view!!.loadDataWithBaseURL("http://winsion.net",
                                    "<script>window.location.href=\"$reloadUrl\";</script>",
                                    "text/html", "utf-8", null)
                            num++
                        }
                    } else {
                        val extraHeaders = HashMap<String, String>()
                        extraHeaders["Referer"] = "http://winsion.net"
                        if (num < 1) {
                            //second reload
                            view!!.loadUrl(reloadUrl, extraHeaders)
                            Log.e(TAG, "extraHeaders======${extraHeaders.size}")
                            num++
                        }
                    }
                }
//*******************test wx pay*************************************************************

//*******************test ali pay************************************************************
                //唤起支付宝app
                if (reloadUrl.startsWith("alipays://platformapi/startApp")) {
                    platformapiUrl = reloadUrl
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reloadUrl))
                    startActivity(intent)
                }
                //网页上点击了"继续支付"
                if (reloadUrl.startsWith("https://mclient.alipay.com/h5Continue.htm?")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(platformapiUrl))
                    startActivity(intent)
                }
//*******************test ali pay************************************************************
                return false
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler!!.proceed()
            }
        }
        //first load
        val url = intent.getStringExtra("url")
        Log.e(TAG, "url======$url")
        webview.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.settings.javaScriptEnabled = false
        webview.stopLoading()
        webview.clearHistory()
        webview.removeAllViews()
        webview.destroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}