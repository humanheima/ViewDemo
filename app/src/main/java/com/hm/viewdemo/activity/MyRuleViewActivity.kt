package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.MyRulerView

/**
 * Created by dumingwei on 2021/7/31
 *
 * Desc: 测试自定义的RulerView
 */
class MyRuleViewActivity : AppCompatActivity() {


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

        myRulerViewOddNumber = findViewById(R.id.my_ruler_view_odd_number)

        myRulerViewEvenNumber = findViewById(R.id.my_ruler_view_even_number)

        myRulerViewOddNumber.setInitialValue(1f, 21f, 1f)

        myRulerViewEvenNumber.setInitialValue(1f, 22f, 1f)
    }

}