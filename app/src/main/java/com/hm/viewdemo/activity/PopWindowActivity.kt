package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_pop_window.*

class PopWindowActivity : AppCompatActivity(), ViewTreeObserver.OnGlobalLayoutListener {


    companion object {

        //打开关闭弹出框
        private const val OPEN_POP = 0
        private const val HIDE_POP = 1


        fun launch(context: Context) {
            val intent = Intent(context, PopWindowActivity::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_window)

        //获取软键盘
        im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val dm = resources.displayMetrics
        //设定一个认为是软键盘弹起的阈值
        softKeyboardHeight = (100 * dm.density).toInt()

        //屏幕高度
        heightPixels = dm.heightPixels
        activityRootView = window.decorView.findViewById(android.R.id.content)
        activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener(this)

    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnStyle1 -> {
                showPopWindow1()
            }
            R.id.btnStyle2 -> {
                //do nothing
            }
        }
    }

    private fun showPopWindow1() {
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
                    Log.d(TAG, "showPopWindow1: KEYCODE_BACK")
                    popupStyle1?.dismiss()
                    true
                }
                false
            }

            popupStyle1 = PopupWindow(
                    contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true
            )
            popupStyle1?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        }
        popupStyle1?.showAtLocation(btnStyle1, Gravity.BOTTOM, 0, 0)
        showKeyBoard()
    }

    override fun onGlobalLayout() {
        //todo 可能会死循环，
        /*activityRootView?.let {
            if (isKeyboardShown(it)) {
                Log.e(TAG, "软键盘弹起")
            } else {
                Log.e(TAG, "软键盘关闭")
                popupStyle1?.dismiss()
            }
        }*/
    }

    private fun isKeyboardShown(rootView: View): Boolean {
        //得到屏幕可见区域的大小
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = heightPixels - r.bottom
        return heightDiff > softKeyboardHeight
    }

    private fun showKeyBoard() {
        im?.let {
            it.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
    }

    private fun hideKeyBoard() {
        im?.hideSoftInputFromWindow(btnStyle1.windowToken, 0)
    }

}
