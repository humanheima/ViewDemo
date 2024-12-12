package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Created by p_dmweidu on 2023/8/10
 * Desc: 1. 测试三角函数相关
 * https://blog.csdn.net/leilifengxingmw/article/details/144409621
 */
class MathTestActivity : AppCompatActivity() {

    companion object {


        private const val TAG = "MathTestActivity"

        fun launch(context: Context) {
            val starter = Intent(context, MathTestActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math_test)
        test()
        Log.e(TAG, "onCreate: ")
        test2()
    }

    /**
     * 测试 基本三角函数
     * sin(30) = 0.5
     * 注意，要把角度转化成弧度
     */
    private fun test() {
        // test: Math.sin(30) = 0.49999999999999994
        Log.i(TAG, "test: Math.sin(30) = ${sin(Math.toRadians(30.0))}")

        Log.i(TAG, "test: Math.cos(60) = ${cos(Math.toRadians(60.0))}")

        //tan(45) = 1.0
        Log.i(TAG, "test: Math.tan(45) = ${tan(Math.toRadians(45.0))}")
    }


    private fun test2() {
        Log.i(TAG, "test2: arcsin(0.5) = ${Math.toDegrees(asin(0.5))}")
        Log.i(TAG, "test2: arccos(0.5) = ${Math.toDegrees(acos(0.5))}")

        Log.i(TAG, "test2: arctan(1.0) = ${Math.toDegrees(atan(1.0))}")
    }

    fun degreesToRadians(degrees: Double): Double {
        return degrees * (Math.PI / 180)
    }


}