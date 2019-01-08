package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_toast.*

class ToastActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ToastActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)
        btnShowToast.setOnClickListener {
            Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show()
        }
        btnShowCustomToast.setOnClickListener { showCustomToast("Hello custom world") }

    }

    override fun onResume() {
        super.onResume()
        Thread(Runnable { runOnUiThread { Log.d(TAG, "runOnUiThread: btnShowToast.height=${btnShowToast.height}") } }).start()
        btnShowToast.post {
            Log.d(TAG, "view post: btnShowToast.height=${btnShowToast.height}")
        }
    }

    private fun showCustomToast(text: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_toast, null)
        val tvMessage = view.findViewById<TextView>(R.id.toast_tv)
        tvMessage.text = text
        //注意这里没有使用Toast.makeText方法
        val toast = Toast(this)
        toast.view = view
        toast.show()
    }
}
