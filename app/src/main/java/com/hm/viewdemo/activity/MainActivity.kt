package com.hm.viewdemo.activity

import android.content.Intent
import android.view.View
import com.hm.viewdemo.R
import com.hm.viewdemo.TestScreenScrollViewActivity
import com.hm.viewdemo.activity.design.CoordinateLayoutActivity
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.bean.Info
import com.hm.viewdemo.bean.Person
import com.hm.viewdemo.custom_view.GetStartAndPracticeActivity
import com.hm.viewdemo.util.ScreenUtil

class MainActivity : BaseActivity() {

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        ScreenUtil.testDensity(this)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnParcelableTest -> {
                testParcelable()
            }
            R.id.btnWebPTest -> {
                WebpTestActivity.launch(this)
            }
            R.id.btnStateBar -> {
                StatusBarActivity.launch(this)
            }
            R.id.btnPopWindow -> {
                PopWindowActivity.launch(this)
            }
            R.id.btnCustomViewGetStart -> {
                GetStartAndPracticeActivity.launch(this)
            }
            R.id.btnSoftKeyboard -> {
                SoftKeyboardActivity.launch(this)
            }
            R.id.btnTestDrawPath -> {
                DrawPathActivity.launch(this)
            }
            R.id.btnTestCountDownTimer -> {
                CountDownTimerActivity.launch(this)
            }
            R.id.btnTestScreenScrollView -> {
                TestScreenScrollViewActivity.launch(this)
            }
            R.id.btnLifecycleView -> {
                ViewLifecycleActivity.launch(this)
            }
            R.id.btnLollipopView -> {
                LollipopActivity.launch(this)
            }
            R.id.btnAnnouncementView -> {
                AnnouncementActivity.launch(this)
            }
            R.id.btnCoordinateLayout -> {
                CoordinateLayoutActivity.launch(this)
            }

            R.id.btnImageViewSrcBackground -> {
                ImageViewSrcBackgroundActivity.launch(this)
            }
            R.id.btnDrawingCache -> {
                TestGetDrawingCachingActivity.launch(this)
            }
            R.id.btnLayoutInflater -> {
                LayoutInflateActivity.launch(this)
                //scroll_activity_main.isNestedScrollingEnabled=false
                //fmLayout.isShouldIntercept = true
            }
            R.id.btnToast -> ToastActivity.launch(this)
            R.id.useHorizontalScrollView -> HorizontalScrollViewActivity.launch(this)
            R.id.btn_card_view -> CardViewActivity.launch(this)
            R.id.btn_test_screen_support -> ScreenSupportActivity.launch(this)
            R.id.btn_about_scroller -> ScrollerActivity.launch(this)
            R.id.btn_time_selector -> TimeSelectorActivity.launch(this)
            R.id.btn_view_stub -> ViewStubActivity.launch(this)
            R.id.btn_horizontal_vertical_conflict -> HorizontalVerticalConflictActivity.launch(this)
            R.id.btn_customer_view -> CustomerViewActivity.launch(this)
            R.id.btn_expandable_listview -> ExpandableListViewActivity.launch(this)
            R.id.btn_vertical_verticla_activity -> VerticalVerticalActivity.launch(this)
            R.id.btn_scrollview_recyclerview_activity -> ScrollViewNestRecyclerViewActivity.launch(this)
            R.id.btn_MaxHeightLayoutActivity -> MaxHeightLayoutActivity.launch(this)
            R.id.btn_bottomSheet -> BottomSheetActivity.launch(this)
            R.id.btn_constraint_layout -> ConstraintActivity.launch(this)
            R.id.btn_autowrap_textview -> AutoWrapTextViewActivity.launch(this)
            R.id.btn_drag_slop_layout -> DragSlopLayoutActivity.launch(this)
            R.id.btn_view_drag_helper -> ViewDragHelperActivity.launch(this)
            R.id.btn_pullrefresh -> PullToRefreshActivity.launch(this)
            R.id.btn_textview -> TextViewActivity.launch(this)
            R.id.btn_histogram_view -> MpAndroidChartActivity.launch(this)
            R.id.btn_test_fudan -> TestFuDanActivity.launch(this)
            R.id.btn_show_loading -> LoadingDialogActivity.launch(this)
            R.id.btn_event_dispatch -> EventDispatchActivity.launch(this)
            R.id.simpleFlowLayout -> SimpleFlowLayoutActivity.launch(this)
            else -> {
                // do nothing
            }
        }

    }

    private fun testParcelable() {

        val intent = Intent(this, ParcelableTestActivity::class.java)

        val arrayList = arrayListOf<Info>()
        for (i in 0..3) {
            val personList = arrayListOf<Person>()
            for (j in 0..3) {
                personList.add(Person(1.63, "dumingwei"))
            }
            val element = Info(
                    personList,
                    "id =$i",
                    "price =$i * 100"
            )
            arrayList.add(element)

        }
        intent.putParcelableArrayListExtra(ParcelableTestActivity.LIST_EXTRA, arrayList)
        intent.putExtra(ParcelableTestActivity.LIST_EXTRA_TWO, arrayList)
        startActivity(intent)
    }

    fun launchRoundImageViewActivity(view: View) {
        RoundImageViewActivity.launch(this)
    }

    fun launchRoundImageViewXfermodeActivity(view: View) {
        RoundImageViewXfermodeActivity.launch(this)
    }

    fun launchXfermodeTestActivity(view: View) {
        XfermodeTestActivity.launch(this)
    }

    fun launchDrawEveryThingActivity(view: View) {
        DrawEveryThingActivity.launch(this)
    }
}
