package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.ListViewAdapter
import com.hm.viewdemo.bean.MyBean
import com.hm.viewdemo.databinding.ActivityListViewFloat2Binding
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2020/4/1
 *
 * Desc: ListView的item的悬浮效果
 */
class ListViewFloat2Activity : AppCompatActivity() {

    private lateinit var list: ArrayList<MyBean>
    private lateinit var adapter: ListViewAdapter

    private val TAG: String? = "ListViewFloatActivity"

    private lateinit var binding: ActivityListViewFloat2Binding

    private lateinit var listView: ListView

    //悬浮的headView的容器
    private lateinit var headViewFloatContainer: FrameLayout


    //ListView添加headView的时候用
    private lateinit var headViewContainerInListView: FrameLayout

    private lateinit var floatHeadView: View

    private lateinit var defaultLp: FrameLayout.LayoutParams

    private var mIsFloatingShowing = false

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ListViewFloat2Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewFloat2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        defaultLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        listView = findViewById(R.id.listView)

        headViewFloatContainer = findViewById(R.id.fl_float_container)



        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "ListView中item点击$position", Toast.LENGTH_SHORT).show()
        }

        addHeadAndFoot()
        //addHeadAndFoot()

//        ivFloatImage.setOnClickListener {
//            Toast.makeText(this, "我是include布局文件中的控件", Toast.LENGTH_SHORT).show()
//            //listView.smoothScrollToPosition(1)
//            listView.setSelection(1)
//        }
        addFloatView()

        useArrayAdapter()

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                Log.i(TAG, "onScroll: firstVisibleItem = $firstVisibleItem")
                if (firstVisibleItem >= 1) {
                    //includeViewFloatLayout.visibility = View.VISIBLE
                } else {
                    //includeViewFloatLayout.visibility = View.GONE
                }
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                //do nothing
            }
        })
        //useSimpleAdapter()
    }

    private fun addFloatView() {
        headViewContainerInListView = FrameLayout(this)
        floatHeadView = View.inflate(this, R.layout.view_float_layout, null)
        floatHeadView.findViewById<ImageView>(R.id.ivFloatImage).setOnClickListener {
            Toast.makeText(this, "做为ListView的item中的控件", Toast.LENGTH_SHORT).show()
        }
        headViewContainerInListView.addView(floatHeadView, defaultLp)
        listView.addHeaderView(headViewContainerInListView)
    }

    private fun showFloatView(showFloatView: Boolean) {
        //避免重复添加移除操作
        if (mIsFloatingShowing == showFloatView) {
            return
        }
        if (showFloatView) {
            mIsFloatingShowing = true
            if (floatHeadView.parent != null) {
                (floatHeadView.parent as? ViewGroup)?.removeView(floatHeadView)
                headViewFloatContainer.addView(floatHeadView, defaultLp)
            }
        } else {
            mIsFloatingShowing = false

        }
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
