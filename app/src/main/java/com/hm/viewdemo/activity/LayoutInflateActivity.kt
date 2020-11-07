package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_layout_inflate.*

/**
 * LayoutInflater源码分析
 */
class LayoutInflateActivity : AppCompatActivity() {

    private val TAG = "LayoutInflateActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LayoutInflateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_inflate)
        val inflater = LayoutInflater.from(this)
        //val bottom=inflater.inflate(R.layout.activity_bottom_sheet,null)
        //val button = inflater.inflate(R.layout.button_layout, null)
        //LayoutInflater.from()
        //clRoot.addView(button)
    }

    /*override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        val view = super.onCreateView(name, context, attrs)
        Log.d(TAG, "onCreateView: $view")//是null
        return view
    }

    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View {
        val view = super.onCreateView(parent, name, context, attrs)
        Log.d(TAG, "onCreateView2: $view")//是null
        return view
    }*/
}
