package com.example.soft_keyboard

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘的基类
 */
abstract class BaseSoftKyeBoardAct : AppCompatActivity() {


    lateinit var rlActionList: RelativeLayout
    lateinit var rlInputNewCategoryName: RelativeLayout
    lateinit var llBottomLayout: LinearLayout

    var activityRootView: View? = null

    //设定一个认为是软键盘弹起的阈值，假的
    private var fakeSoftKeyboardHeight: Int = 0
    private var screenHeightPixels: Int = 0
    private var im: InputMethodManager? = null


    private val TAG = "BaseSoftKyeBoardAct"

    //记录原始窗口高度
    var mWindowHeight = 0
    var mSoftKeyboardHeight = 0

    var onKeyboardChangeListener: OnKeyboardChangeListener? = null

    private val mGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener =
        object : ViewTreeObserver.OnGlobalLayoutListener {

            override
            fun onGlobalLayout() {
                val r = Rect()
                //获取当前窗口实际的可见区域，不包括顶部的状态栏
                window.decorView.getWindowVisibleDisplayFrame(r)
                val height: Int = r.height()
                if (mWindowHeight == 0) {
                    //一般情况下，这是原始的窗口高度
                    mWindowHeight = height
                } else {
                    if (mWindowHeight != height) {
                        //两次窗口高度相减，就是软键盘高度
                        mSoftKeyboardHeight = mWindowHeight - height
                        Log.i(TAG, "onGlobalLayout: SoftKeyboard height = $mSoftKeyboardHeight")
                    }

                }

                val heightDiff = screenHeightPixels - r.bottom
                Log.i(TAG, "onGlobalLayout: heightDiff = $heightDiff")
                if (heightDiff > fakeSoftKeyboardHeight) {
                    Log.i(TAG, "onGlobalLayout: 软键盘弹起")
                    onKeyboardChangeListener?.onKeyboardChange(true, mSoftKeyboardHeight)
                } else {
                    Log.i(TAG, "onGlobalLayout: 软键盘关闭")
                    onKeyboardChangeListener?.onKeyboardChange(false, 0)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        val dm = resources.displayMetrics
        //设定一个认为是软键盘弹起的阈值
        fakeSoftKeyboardHeight = (100 * dm.density).toInt()

        //屏幕高度
        screenHeightPixels = dm.heightPixels
        Log.i(TAG, "onCreate: screenHeightPixels = $screenHeightPixels")

        im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        onKeyboardChangeListener = object : OnKeyboardChangeListener {

            override fun onKeyboardChange(isShow: Boolean, softKeyboardHeight: Int) {
                Log.i(
                    TAG,
                    "onKeyboardChange: isShow = $isShow softKeyboardHeight= $softKeyboardHeight"
                )
                if (isShow) {
                    if (softKeyboardHeight > 0) {
                        if (llBottomLayout.paddingBottom != softKeyboardHeight) {
                            Log.e(
                                TAG,
                                "onKeyboardChange: 键盘弹起重新设置paddingBottom=$softKeyboardHeight"
                            )
                            llBottomLayout.setPadding(0, 0, 0, softKeyboardHeight)
                        }
                    } else {
                        if (llBottomLayout.paddingBottom != 0) {
                            Log.e(TAG, "onKeyboardChange: 键盘弹起重新设置paddingBottom = 0")
                            llBottomLayout.setPadding(0, 0, 0, 0)
                        }
                    }
                } else {
                    if (llBottomLayout.paddingBottom != 0) {
                        Log.e(TAG, "onKeyboardChange: 键盘关闭重新设置paddingBottom=0")
                        llBottomLayout.setPadding(0, 0, 0, 0)
                    }
                }
            }
        }
        activityRootView = window.decorView.findViewById(android.R.id.content)
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)

        rlActionList = findViewById(R.id.rl_action_list)
        rlInputNewCategoryName = findViewById(R.id.rl_input_new_category_name)
        llBottomLayout = findViewById(R.id.ll_bottom_layout)

        findViewById<TextView>(R.id.tv_rename_category).setOnClickListener {
            rlActionList.visibility = View.GONE
            rlInputNewCategoryName.visibility = View.VISIBLE
            showKeyBoard()
        }
        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            rlActionList.visibility = View.VISIBLE
            rlInputNewCategoryName.visibility = View.GONE
            hideKeyBoard(rlInputNewCategoryName)
        }
    }

    abstract fun getLayoutResId(): Int

    override fun onDestroy() {
        super.onDestroy()
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener)
    }

    fun showKeyBoard() {
        im?.toggleSoftInput(0, 0)
    }

    fun hideKeyBoard(view: View) {
        im?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    interface OnKeyboardChangeListener {

        /**
         * @param isShow           软键盘是否显示
         * @param softKeyboardHeight 软键盘高度，当软键盘隐藏时，高度为0
         */
        fun onKeyboardChange(isShow: Boolean, softKeyboardHeight: Int)
    }

}