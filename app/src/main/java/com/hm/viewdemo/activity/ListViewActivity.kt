package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.ListViewAdapter
import com.hm.viewdemo.bean.MyBean
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_list_view.*

/**
 * Created by dumingwei on 2020/4/1
 *
 * Desc: ListView的使用
 *
 * 参考链接：https://www.jianshu.com/p/08e6c83ff2b7
 */
class ListViewActivity : AppCompatActivity() {


    private val TAG: String = "ListViewActivity"

    private lateinit var list: ArrayList<MyBean>
    private lateinit var adapter: ListViewAdapter

    private var currentScrollY = 0

    var sparseArray: SparseArray<String> = SparseArray()

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ListViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        sparseArray.put(4, "4")
        sparseArray.put(0, "0")
        sparseArray.put(2, "2")
        sparseArray.put(3, "3")

        for (i in 0 until sparseArray.size()) {
            Log.i(TAG, "onCreate: ${sparseArray[i]}")
        }

        listView.setOnItemClickListener { parent, view, position, id ->

            Toast.makeText(this, "position = $position", Toast.LENGTH_SHORT).show()
        }

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {

            // 创建一个稀疏数组，用于存储Item的高度和mTop
            private val recordSp: SparseArray<ItemRecord> = SparseArray()
            private var mCurrentFirstVisibleItem = 0

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                Log.i(TAG, "onScroll: ${view.scrollY}")
                //do nothing
                mCurrentFirstVisibleItem = firstVisibleItem
                val firstView = view.getChildAt(0)

                if (null != firstView) {
                    var itemRecord = recordSp.get(firstVisibleItem)
                    if (null == itemRecord) {
                        itemRecord = ItemRecord()
                    }
                    itemRecord.height = firstView.height
                    //top值总是小于或等于0的
                    itemRecord.top = firstView.top

                    /**
                     * 将当前第一个可见Item的高度和top存入SparseArray中，
                     * SparseArray的key是Item的position
                     */
                    recordSp.append(firstVisibleItem, itemRecord)
                    currentScrollY = getScrollY()

                    Log.i(TAG, "onScroll: scrollY =  $currentScrollY")

                    if (currentScrollY > 0) {
                        adsorbView.setSuction(true)
                    }
                }
            }

            private fun getScrollY(): Int {
                var height = 0
                /**
                 * for循环累加所有划出屏幕的View的高度
                 */
                for (i in 0 until mCurrentFirstVisibleItem) {
                    val itemRecord = recordSp.get(i)
                    if (itemRecord != null) {
                        height += itemRecord.height
                    }
                }
                //取出当前第一个可见Item的ItemRecord对象
                var itemRecord = recordSp.get(mCurrentFirstVisibleItem)
                if (null == itemRecord) {
                    itemRecord = ItemRecord()
                }
                //在加上当前第一个可见的item划出屏幕的高度
                //由于存入的top值是小于或等于0的，这里是减去top值而不是加
                return height - itemRecord.top
            }

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                val statusString = when (scrollState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> "SCROLL_STATE_TOUCH_SCROLL"
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> "SCROLL_STATE_FLING"
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> "SCROLL_STATE_IDLE"
                    else -> "SCROLL_STATE_OTHER"
                }
                Log.i(TAG, "onScrollStateChanged: scrollState = $statusString")
                when (scrollState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                        //Log.i(TAG, "onScrollStateChanged: state = SCROLL_STATE_TOUCH_SCROLL ${getScrollY()}")
                    }
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        //Log.i(TAG, "onScrollStateChanged: state = SCROLL_STATE_IDLE")
                        adsorbView.setSuction(false)

                        val view = listView.getChildAt(listView.childCount - 1)

                        Log.i(TAG, "onScrollStateChanged: view.bottom = ${view.bottom}")
                        Log.i(TAG, "onScrollStateChanged: listView.height = ${listView.height}")
                    }
                }
            }
        })

        //addHeadAndFoot()
        //addHeadAndFoot()

        useArrayAdapter()
        //useSimpleAdapter()
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
        listView.adapter = adapter
    }

    private fun useArrayAdapter() {
        list = arrayListOf()
        for (i in 0..200) {
            val bean = MyBean("title$i", "detail$i")
            list.add(bean)
        }
        adapter = ListViewAdapter(this, R.layout.item_list_view, list)

        listView.adapter = adapter

        adapter.notifyDataSetChanged()
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

class ItemRecord {
    var height = 0
    var top = 0
}
