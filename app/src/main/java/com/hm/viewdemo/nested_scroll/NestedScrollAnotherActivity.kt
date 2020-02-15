package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.hm.viewdemo.R
import com.zhy.base.adapter.ViewHolder
import com.zhy.base.adapter.recyclerview.CommonAdapter
import kotlinx.android.synthetic.main.activity_nested_scroll_another.*
import java.util.ArrayList

class NestedScrollAnotherActivity : AppCompatActivity() {

    private val mDatas = ArrayList<String>()

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollAnotherActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_another)

        rv.layoutManager = LinearLayoutManager(this)
        for (i in 0..49) {
            mDatas.add("Defaut Value -> $i")
        }
        rv.adapter = object : CommonAdapter<String>(this, R.layout.nested_rv_item, mDatas) {
            override fun convert(holder: ViewHolder, o: String) {
                holder.setText(R.id.id_info, o)
            }
        }

    }
}
