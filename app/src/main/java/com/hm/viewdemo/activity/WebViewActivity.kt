package com.hm.viewdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2025/11/24
 * Desc: 测试兔小巢
 */
class WebViewActivity : AppCompatActivity() {


    private val TAG = "WebViewActivity"


    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnPostForm: Button
    private lateinit var btnPostJson: Button
    private lateinit var btnBack: Button
    private lateinit var btnForward: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnCallJs: Button

    // 测试网页（可替换为本地HTML或其他网络地址）
    // 新增：POST 请求测试地址（替换为你的实际接口）
    private val postFormUrl = "https://support.qq.com/product/790831" // 表单格式POST接口
    private val postJsonUrl = "https://support.qq.com/product/790831" // JSON格式POST接口
    private val testUrl = "https://support.qq.com/product/790831" // 网络网页
    // private val testUrl = "file:///android_asset/test.html" // 本地HTML（需在assets文件夹创建）


    companion object {


        fun launch(context: Context) {
            val starter = Intent(context, WebViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // 初始化控件
        initViews()

        // 1. 配置WebView设置
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true // 允许JS执行（关键）
        webSettings.domStorageEnabled = true // 启用DOM存储（解决部分网页加载异常）
        webSettings.allowFileAccess = true // 允许访问文件
        webSettings.allowContentAccess = true // 允许访问内容
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // 缓存模式
        webSettings.userAgentString += " AndroidWebViewDemo" // 自定义UA

        // 2. 解决Android 5.0+ 网页图片不显示问题
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 3. 添加JS调用Android的接口（name为"AndroidInterface"，JS中通过该名称调用）
        //webView.addJavascriptInterface(AndroidToJs(), "AndroidInterface")

        // 4. 设置WebViewClient（控制页面跳转、加载状态）
        webView.webViewClient = object : WebViewClient() {
            // 页面开始加载时回调
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE // 显示进度条
            }

            // 页面加载完成时回调
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE // 隐藏进度条

                webView.evaluateJavascript("document.body.innerText") { response ->
                    Log.d("WebViewPOST", "接口响应：$response")
                    // 解析 response（注意：response 是带引号的字符串，需去除引号）
                }
                // 更新按钮状态（前进/后退是否可用）
                updateButtonStatus()
            }

            // 拦截网页跳转（在WebView内打开，不调用系统浏览器）
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            // 加载错误时回调
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // 显示错误提示或加载本地错误页面
                Toast.makeText(this@WebViewActivity, "页面加载失败", Toast.LENGTH_SHORT).show()
                // webView.loadUrl("file:///android_asset/error.html") // 可选：加载本地错误页面
            }
        }

        // 5. 设置WebChromeClient（处理进度、标题等）
        webView.webChromeClient = object : WebChromeClient() {
            // 加载进度更新（0-100）
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
            }

            // 网页标题更新
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title // 设置ActionBar标题为网页标题
            }
        }

        // 6. 加载网页
        //webView.loadUrl(testUrl)

        // 7. 绑定按钮点击事件
        bindButtonEvents()
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        btnPostForm = findViewById(R.id.btnPostForm)
        btnPostJson = findViewById(R.id.btnPostJson)
        btnBack = findViewById(R.id.btnBack)
        btnForward = findViewById(R.id.btnForward)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnCallJs = findViewById(R.id.btnCallJs)
    }

    private fun bindButtonEvents() {
        // 提交表单格式POST数据
        btnPostForm.setOnClickListener {
            postFormData()
        }
        // 提交JSON格式POST数据
        btnPostJson.setOnClickListener {
            postJsonData()
        }
        // 后退
        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                Toast.makeText(this, "已无法后退", Toast.LENGTH_SHORT).show()
            }
        }

        // 前进
        btnForward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                Toast.makeText(this, "已无法前进", Toast.LENGTH_SHORT).show()
            }
        }

        // 刷新
        btnRefresh.setOnClickListener {
            webView.reload()
        }

        // Android调用JS方法
        btnCallJs.setOnClickListener {
            // 调用JS的showToast方法（参数为"Hello from Android"）
            webView.post {
                webView.evaluateJavascript("javascript:showToast('Hello from Android')") { result ->
                    // JS方法的返回值（result为JSON格式字符串）
                    Toast.makeText(this, "JS返回结果：$result", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 更新前进/后退按钮状态
    private fun updateButtonStatus() {
        btnBack.isEnabled = webView.canGoBack()
        btnForward.isEnabled = webView.canGoForward()
    }

    // 重写返回键：优先后退WebView页面，而非退出Activity
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed() // 无页面可退时退出Activity
        }
    }

    /**
     * 场景1：提交表单格式的 POST 数据
     * 格式：username=test&password=123456（需 URLEncoder 编码特殊字符）
     */
    private fun postFormData() {
        // 1. 构建 POST 参数（键值对）
        val params = mutableMapOf(
            "username" to "android_webview_test",
            "password" to "123456",
            "age" to "25"
        )

        val openid = "246245927461851136" // 用户的openid
        val nickname = "戏友5790" // 用户的nickname
        val headimgurl =
            "https://staticcdn-test.rolepub.com/13af773c478235e6228a6907a3b9da3c.jpg" // 用户的头像url

        val secretKey = "UoRH8417"
        /* 准备post参数 */
        val signature: String = "$openid$nickname$headimgurl$secretKey"
        val md5String = MD5Utils2.md5Encode(signature)

        // 2. 拼接参数为 "key1=value1&key2=value2" 格式，并 URLEncoder 编码
//        val postDataStr = params.entries.joinToString("&") { entry ->
//            "${URLEncoder.encode(entry.key, "UTF-8")}=${URLEncoder.encode(entry.value, "UTF-8")}"
//        }

        // 3. 转换为字节数组（必须指定 UTF-8 编码）
        //val postData = postDataStr.toByteArray(charset("UTF-8"))

        // 4. 配置 POST 请求头（关键：指定 Content-Type）
//        webView.settings.setRequestHeader(
//            "Content-Type",
//            "application/x-www-form-urlencoded; charset=UTF-8"
//        )

        //webView.loadUrl(postFormUrl)
        // 5. 发起 POST 请求
        val postData2: String =
            "nickname=$nickname&avatar=$headimgurl&openid=$openid&user_signature=$md5String"

        webView.postUrl(postFormUrl, postData2.toByteArray(charset("UTF-8")))
        //Log.d("WebViewPOST", "表单POST参数：$postDataStr")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postJsonData() {
        // 2. 转换为字节数组（UTF-8 编码）
       // val postData = NativeAesEncryptUtils.simulateEncryptTransfer()
        val postData = LoginStateEncryptUtils.main()
        //val postData ="6eOq-S7hW0wZKFgPRl6hnx7jhxAv4ULv20eD-Y4D5IeqwDLx5oswky16IkBMk0wV1Z_tAgK8eibpLszJIBXWONHvYzF5XFgoGwdZRWyMQvX82K2Y0CZVknbRgForgzuC8ap0R3i63gVnvOFVQZwoUnnnKKmhp5o755Wf19jXTgAFT9Hc6_4nFx3e0NEkQnh1Mg471nYC3jpzwe0S75ie4pwG7kOrojkn8TP368Hz3HyVzGwixuPRm_1IrI9o52CX_V12iS6_hIFSke2b_eviZw"

        // 3. 配置 POST 请求头（关键：指定 Content-Type 为 application/json）
//        webView.settings.setRequestHeader(
//            "Content-Type",
//            "application/json; charset=UTF-8"
//        )

        // 4. 发起 POST 请求
        webView.postUrl(postJsonUrl, postData.toByteArray())
        //Log.d("WebViewPOST", "JSON POST参数：$jsonData")
    }


    // 释放WebView资源（防止内存泄漏）
    override fun onDestroy() {
        super.onDestroy()
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.stopLoading()
        webView.removeAllViews()
        webView.destroy()
    }

    // JS调用Android的接口类（必须加@JavascriptInterface注解）
    inner class AndroidToJs {
        // JS调用该方法（无参数）
        @JavascriptInterface
        fun showAndroidToast() {
            runOnUiThread { // 切换到主线程（JS调用是子线程）
                Toast.makeText(this@WebViewActivity, "JS调用了Android的方法", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // JS调用该方法（带参数）
        @JavascriptInterface
        fun showAndroidToastWithParam(msg: String) {
            runOnUiThread {
                Toast.makeText(this@WebViewActivity, "JS传参：$msg", Toast.LENGTH_SHORT).show()
            }
        }

        // JS调用该方法（带返回值）
        @JavascriptInterface
        fun getAndroidData(): String {
            return "我是Android返回给JS的数据"
        }
    }
}