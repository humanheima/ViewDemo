package com.example.soft_keyboard

import android.content.Context
import android.content.Intent


/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘Adjustpan的Activity
 *
 * 非全屏模式下，使用 AdjustPan 可以和全屏模式下的处理方式一致
 */
class SoftAdjustpanActivity : BaseSoftKyeBoardAct() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, SoftAdjustpanActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_soft_adjustpan
    }

}