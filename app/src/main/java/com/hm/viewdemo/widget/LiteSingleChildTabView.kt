package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.LiteChildTab
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2021/6/16.
 *
 * Desc: 子tab单独的View
 */
class LiteSingleChildTabView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val TAG: String = "LiteSingleChildTabView"

    private var tvChildTabTitle: TextView? = null
    private var bgChildTabView: View? = null

    private var selectedColor: Int = context.resources.getColor(R.color.main_color_272727)
    private var unSelectedColor: Int = context.resources.getColor(R.color.main_color_666666)

    private var selectedTextSize: Int = 18
    private var unSelectedTextSize: Int = 14

    private var dp8: Int = 0


    private var mTabData: LiteChildTab? = null

    private var mDrawable: Drawable? = null

    init {

        View.inflate(context, R.layout.main_item_single_child_tab, this)
        tvChildTabTitle = findViewById(R.id.main_tv_child_tab_title)
        bgChildTabView = findViewById(R.id.main_view_tab_bg)

        dp8 = ScreenUtil.dpToPx(context, 8)
    }

    fun setData(tab: LiteChildTab, drawable: Drawable?) {
        mTabData = tab
        mDrawable = drawable
        tvChildTabTitle?.text = tab.title
    }

    fun getData(): LiteChildTab? {
        return mTabData
    }

    /**
     * @param selected 是否选中
     */
    fun setSelectedStatus(selected: Boolean) {
        if (mTabData == null) {
            return
        }
        mTabData?.selectd = selected
        if (selected) {
            tvChildTabTitle?.setTextColor(selectedColor)
            tvChildTabTitle?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, selectedTextSize.toFloat())
            tvChildTabTitle?.typeface = Typeface.DEFAULT_BOLD
            bgChildTabView?.visibility = View.VISIBLE
            bgChildTabView?.background = mDrawable

            //mDrawable?.setBounds(0, 0, bgChildTabView?.width ?: 0,  dp8)


        } else {
            tvChildTabTitle?.setTextColor(unSelectedColor)
            tvChildTabTitle?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, unSelectedTextSize.toFloat())
            tvChildTabTitle?.typeface = Typeface.DEFAULT
            bgChildTabView?.background = null
            bgChildTabView?.visibility = View.GONE
        }
    }

}