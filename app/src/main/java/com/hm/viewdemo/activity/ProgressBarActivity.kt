package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityProgressBarBinding
import java.lang.ref.WeakReference

/**
 * Created by dumingwei on 2020/4/15
 *
 *
 * Desc: 测试loading dialog progress等
 * 参考链接：https://blog.csdn.net/mad1989/article/details/38042875
 */
class ProgressBarActivity : BaseActivity<ActivityProgressBarBinding>() {

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, ProgressBarActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var handler: MyHandler? = null
    override fun createViewBinding(): ActivityProgressBarBinding {
        return ActivityProgressBarBinding.inflate(layoutInflater)
    }

    override fun initData() {
        handler = MyHandler(this)
        Thread {
            while (true) {
                for (i in 0..100) {
                    try {
                        Thread.sleep(50)
                        val msg = handler!!.obtainMessage()
                        msg.arg1 = i
                        handler!!.sendMessage(msg)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
        binding.btnChangeRate.setOnClickListener { binding.ratingBar.rating = 3.5f }
//        val drawable = binding.progressBar2.progressDrawable
//        if (drawable is AnimatedVectorDrawable){
//            drawable.setduration(1000)
//        }
    }

    private class MyHandler(baseActivity: BaseActivity<*>) : Handler() {

        private val weakActivity: WeakReference<BaseActivity<*>> = WeakReference(baseActivity)

        override fun handleMessage(msg: Message) {
            val activity = weakActivity.get() as ProgressBarActivity?
            if (activity != null) {
                activity.binding.progressBar.progress = msg.arg1
                activity.binding.textProgress.text = msg.arg1.toString() + "%"
            }
        }
    }

}
