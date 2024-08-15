package com.hm.viewdemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hm.viewdemo.widget.LoadingDialog

/**
 * Created by dumingwei on 2017/2/26.
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {


    protected val TAG = javaClass.simpleName
    private var loadingDialog: LoadingDialog? = null

    protected lateinit var binding: T
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding()
        setContentView(binding.root)
        initData()
    }

    protected abstract fun createViewBinding(): T
    protected abstract fun initData()
    protected open fun bindEvent() {}
    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }

    fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }
}
