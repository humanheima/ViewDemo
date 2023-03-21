package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_pop_window_with_input.*

/**
 * Created by dumingwei on 2020/11/13
 *
 * Desc: PopWindow中有输入框的情景
 * 参考链接：https://blog.csdn.net/xiaoliluote/article/details/99549281
 */
class PopWindowWithInputActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PopWindowWithInputActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val TAG = "PopWindowActivity"

    private var popupStyle1: PopupWindow? = null
    private lateinit var tvCancel: TextView
    private var etName: EditText? = null
    private lateinit var tvConfirm: TextView

    private var popupStyle2: PopupWindow? = null

    private var activityRootView: View? = null

    //设定一个认为是软键盘弹起的阈值
    private var softKeyboardHeight: Int = 0
    private var heightPixels: Int = 0

    private var im: InputMethodManager? = null

    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_window_with_input)

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.i(TAG, "handleMessage: ${msg.what}")
                handler.sendEmptyMessageDelayed(1, 200)
            }
        }

        //获取软键盘
        im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val dm = resources.displayMetrics
        //设定一个认为是软键盘弹起的阈值
        softKeyboardHeight = (100 * dm.density).toInt()

        //屏幕高度
        heightPixels = dm.heightPixels
        activityRootView = window.decorView.findViewById(android.R.id.content)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnStyle1 -> {
                showPopWindow1()
            }
        }
    }

    private fun showPopWindow1() {
        handler.sendEmptyMessageDelayed(1, 200)
        if (popupStyle1 == null) {
            val contentView = LayoutInflater.from(this).inflate(R.layout.pop_modify_nickname, null)
            tvConfirm = contentView.findViewById(R.id.tvConfirm)
            tvCancel = contentView.findViewById(R.id.tvCancel)

            tvConfirm.setOnClickListener {
                hideKeyBoard()
                popupStyle1?.dismiss()

            }

            tvCancel.setOnClickListener {
                hideKeyBoard()
                popupStyle1?.dismiss()
            }

            etName = contentView.findViewById(R.id.etNickname)

            etName?.setOnKeyListener { v, keyCode, event ->


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //手机的返回键
                    Log.i(TAG, "showPopWindow1: KEYCODE_BACK")
                    popupStyle1?.dismiss()
                    true
                }
                false
            }

            popupStyle1 = PopupWindow(
                contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popupStyle1?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        }
        popupStyle1?.showAtLocation(btnStyle1, Gravity.BOTTOM, 0, 0)
        handler.postDelayed({
            showKeyBoard()
        }, 200)
        //showKeyBoard()
    }

    private fun showKeyBoard() {
        im?.let {
            it.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
    }

    private fun hideKeyBoard() {
        im?.hideSoftInputFromWindow(btnStyle1.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

}
