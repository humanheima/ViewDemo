package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.SendOptionsBean
import kotlinx.android.synthetic.main.activity_draw_every_thing.*
import java.util.*

class DrawEveryThingActivity : AppCompatActivity() {


    private val TAG: String? = "DrawEveryThingActivity"

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DrawEveryThingActivity::class.java)
            context.startActivity(intent)
        }
    }


    lateinit var stage3List: MutableList<SendOptionsBean>

    lateinit var stage6List: MutableList<SendOptionsBean>

    private var stage3FinishProgress = 0f

    private var stage6FinishProgress = 0f

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_every_thing)

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(TAG, "handleMessage: ")
                when (msg.what) {
                    3 -> {
                        updateStage3()

                    }
                    6 -> {
                        updateStage6()

                    }
                }
            }
        }

        stage3List = initStage3List()
        stage6List = initStage6List()

        setData()

        btnUpdateStage3.setOnClickListener {
            stage3FinishProgress = 0f
            updateStage3()
        }

        btnUpdateStage6.setOnClickListener {
            stage6FinishProgress = 0f

            updateStage6()

        }
    }

    private fun setData() {
        stageAwardView0.setData(stage3List, 3, 1f)

        stageAwardView1.setData(stage6List, 6, 1f)
    }

    /**
     * 更新第4阶段
     */
    private fun updateStage3() {
        stage3FinishProgress += 0.01f
        Log.d(TAG, "updateStage3: fl = $stage3FinishProgress")
        if (stage3FinishProgress - 1f > 0.001) {
            return
        }
        stageAwardView0.setData(stage3List, 3, stage3FinishProgress)
        handler.sendEmptyMessageDelayed(3, 50)
    }

    /**
     * 更新第7阶段
     */
    private fun updateStage6() {
        stage6FinishProgress += 0.01f
        Log.d(TAG, "updateStage6: fl = $stage6FinishProgress")
        if (stage6FinishProgress - 1f > 0.001) {
            return
        }
        stageAwardView1.setData(stage6List, 6, stage6FinishProgress)
        handler.sendEmptyMessageDelayed(6, 50)
    }

    private fun initStage3List(): MutableList<SendOptionsBean> {
        val list: MutableList<SendOptionsBean> = ArrayList()
        for (i in 0 until 9) {
            val optionsBean = SendOptionsBean()
            optionsBean.segValue = 100 * i
            optionsBean.setSegKey(i + 0.5)
            if (i == 0) {
                optionsBean.setStatus(1)
            } else if (i < 3) {
                optionsBean.setStatus(2)
            }
            list.add(optionsBean)
        }
        return list
    }

    private fun initStage6List(): MutableList<SendOptionsBean> {
        val list: MutableList<SendOptionsBean> = ArrayList()
        for (i in 0 until 9) {
            val optionsBean = SendOptionsBean()
            optionsBean.segValue = 100 * i
            optionsBean.setSegKey(i + 0.5)
            if (i == 0) {
                optionsBean.setStatus(1)
            } else if (i < 6) {
                optionsBean.setStatus(2)
            }
            list.add(optionsBean)
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}