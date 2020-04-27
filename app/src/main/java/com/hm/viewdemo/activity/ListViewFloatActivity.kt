package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.ListViewAdapter
import com.hm.viewdemo.bean.MyBean
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_list_view_float.*
import kotlinx.android.synthetic.main.view_float_layout.*

/**
 * Created by dumingwei on 2020/4/1
 *
 * Desc: ListView的item的悬浮效果
 */
class ListViewFloatActivity : AppCompatActivity() {


    private lateinit var list: ArrayList<MyBean>
    private lateinit var adapter: ListViewAdapter

    private val TAG: String? = "ListViewFloatActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ListViewFloatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_float)
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "ListView中item点击", Toast.LENGTH_SHORT).show()
        }

        addHeadAndFoot()
        //addHeadAndFoot()

        ivFloatImage.setOnClickListener {
            Toast.makeText(this, "我是include布局文件中的控件", Toast.LENGTH_SHORT).show()
        }
        addFloatView()

        useArrayAdapter()



        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                Log.d(TAG, "onScroll: firstVisibleItem = $firstVisibleItem")
                if (firstVisibleItem >= 1) {
                    includeViewFloatLayout.visibility = View.VISIBLE
                } else {
                    includeViewFloatLayout.visibility = View.GONE
                }
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                //do nothing
            }
        })
        //useSimpleAdapter()
    }

    private fun addFloatView() {
        val floatView = View.inflate(this, R.layout.view_float_layout, null)
        floatView.findViewById<ImageView>(R.id.ivFloatImage).setOnClickListener {
            Toast.makeText(this, "做为ListView的item中的控件", Toast.LENGTH_SHORT).show()
        }

        listView.addHeaderView(floatView)
    }

    private fun useSimpleAdapter() {

        val images = arrayOf(
                R.drawable.ic_dog,
                R.mipmap.ic_launcher,
                R.drawable.ic_dog,
                R.mipmap.ic_launcher,
                R.drawable.ic_dog,
                R.mipmap.ic_launcher,
                R.drawable.ic_dog,
                R.mipmap.ic_launcher,
                R.drawable.ic_dog
        )

        val mSimpleList = ArrayList<Map<String, *>>()

        for (i in images.indices) {
            val map = hashMapOf<String, Any>()
            map.put("img", images[i])
            map.put("text", "item$i")
            mSimpleList.add(map)

        }

        /**
         * 将数据源的数据加载到适配器中 SimpleAdapter context: 上下文对象 data：表示加载到适配器中的数据对象
         * resource： 表示adapter控件中每项资源id from:表示数据源map 中key 的数组，表示key指定的值
         * to：表示需要展示对应数据的控件资源id
         *
         * 通过from 和to的对应，将from 中key值对应的数据指定的显示到to 指定资源id的控件中
         *
         * **/

        val adapter = SimpleAdapter(this, mSimpleList, R.layout.item_simple_adapter,
                arrayOf("img", "text"), intArrayOf(R.id.img, R.id.tv))
        // 将适配器中的数据添加到控件中
        listView.adapter = adapter;
    }

    private fun useArrayAdapter() {
        list = arrayListOf()
        for (i in 0..20) {
            val bean = MyBean("title$i", "detail$i")
            list.add(bean)
        }
        adapter = ListViewAdapter(this, R.layout.item_list_view, list)

        listView.adapter = adapter
    }


    private fun addHeadAndFoot() {
        val head = ImageView(this)
        head.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtil.dpToPx(this, 200))
        head.scaleType = ImageView.ScaleType.CENTER_CROP
        head.setImageResource(R.drawable.balloon)

        listView.addHeaderView(head)


        val foot = TextView(this)
        foot.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtil.dpToPx(this, 48))
        foot.textSize = 16f

        foot.gravity = Gravity.CENTER
        foot.text = "Foot View"
        listView.addFooterView(foot)
    }


}
