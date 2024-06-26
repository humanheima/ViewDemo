package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.MyRulerView
import com.hm.viewdemo.widget.MyRulerViewPractice
import java.text.DecimalFormat

/**
 * Created by dumingwei on 2021/7/31
 *
 * Desc: 测试自定义的RulerView
 */
class MyRuleViewActivity : AppCompatActivity() {


    private val TAG: String = "MyRuleViewActivity"

    private lateinit var myRulerViewPractice: MyRulerViewPractice

    private lateinit var myRulerViewOddNumber: MyRulerView
    private lateinit var myRulerViewEvenNumber: MyRulerView

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, MyRuleViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rule_view)

        myRulerViewPractice = findViewById(R.id.my_ruler_view_practice)

        myRulerViewOddNumber = findViewById(R.id.my_ruler_view_odd_number)

        myRulerViewEvenNumber = findViewById(R.id.my_ruler_view_even_number)


        val startNum = 5f
        val endNum = 30f
        //size是26
        val size = (endNum - startNum).toInt() + 1
        val floatTextArray = FloatArray(size)

        val decimalFormat = DecimalFormat(".0")
        for (i in 0 until size) {
            floatTextArray[i] = decimalFormat.format(0.5f + i * 0.1f).toFloat()
        }

        floatTextArray.forEachIndexed { index, fl ->
            Log.i(TAG, "onCreate: $index  $fl")
        }

        myRulerViewPractice.setInitialValue(startNum, endNum, 10f, 1f, floatTextArray)

        myRulerViewPractice.onNumSelectListener = object : MyRulerViewPractice.OnNumSelectListener {
            override fun onNumSelect(selectedNum: Float) {
                Log.i(TAG, "onNumSelect: $selectedNum")
                //这个震动效果比较强烈
                //myRulerViewPractice.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                //这个震动效果比较轻微
                myRulerViewPractice.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
        }

        myRulerViewOddNumber.setInitialValue(1f, 40f, 10f, 1f)

        myRulerViewOddNumber.onNumSelectListener = object : MyRulerView.OnNumSelectListener {
            override fun onNumSelect(selectedNum: Float) {
                Log.i(TAG, "onNumSelect: $selectedNum")
            }
        }
        myRulerViewEvenNumber.setInitialValue(1f, 21f, 12f, 1f)
    }

}