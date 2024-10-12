package com.hm.viewdemo.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.res.AssetManager
import android.os.Debug
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soft_keyboard.TestSoftKeyboardActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.RoundViewActivity
import com.hm.viewdemo.TestScreenScrollViewActivity
import com.hm.viewdemo.activity.compose.WidgetsActivity
import com.hm.viewdemo.activity.design.CoordinateLayoutActivity
import com.hm.viewdemo.activity.design.PinScrollTextActivity
import com.hm.viewdemo.activity.design.TabLayoutActivity
import com.hm.viewdemo.activity.textview.FontMetricsActivity
import com.hm.viewdemo.animtest.ActivityAnimTestActivity
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.bean.Info
import com.hm.viewdemo.bean.Person
import com.hm.viewdemo.cancel_event.TestCancelEventActivity
import com.hm.viewdemo.custom_view.GetStartAndPracticeActivity
import com.hm.viewdemo.databinding.ActivityMainBinding
import com.hm.viewdemo.day_night.ChangeDayNightThemeActivity
import com.hm.viewdemo.moneyscale.MoneyScaleMainActivity
import com.hm.viewdemo.nested_scroll.NestedScrollMainActivity
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.ChatThreeAvatarView
import pub.devrel.easypermissions.EasyPermissions
import kotlin.random.Random

/**
 * Created by p_dmweidu on 2023/8/21
 * Desc:
 * 测试Switch 的用法： https://juejin.cn/post/6970959897575817224
 */
class MainActivity : BaseActivity<ActivityMainBinding>(), EasyPermissions.PermissionCallbacks {

    var perms = arrayOf(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    private lateinit var btnRoundProgressView: Button


    private val avatarList = mutableListOf<String>()

    private var chatThreeAvatarView: ChatThreeAvatarView? = null

    override fun createViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        val filesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (filesDir != null) {
            val filePath = "$filesDir/mytrace.trace"
            Debug.startMethodTracing(filePath)
        }
        if (!EasyPermissions.hasPermissions(this, perms[0], perms[1])) {
            EasyPermissions.requestPermissions(this, "I need permission!", 100, perms[0], perms[1])
        }
        ScreenUtil.testDensity(this)
        //BlockDetectByPrinter.start()

        Debug.stopMethodTracing()

        btnRoundProgressView = findViewById(R.id.btn_round_progress_view);
        btnRoundProgressView.scrollX = 100

        binding.scrollViewRoot.post {
            Log.e(
                TAG,
                "initData: scroll_view_root.measuredHeight = ${binding.scrollViewRoot.measuredHeight}"
            )
            Log.e(TAG, "initData: fl_content.measuredHeight = ${binding.flContent.measuredHeight}")
        }


        avatarList.add("https://xxvirtualcharactercdn.rongshuxia.com/7F2B7CCCC90CFD154497A93A7CA147FC.jpg")
        avatarList.add("https://xxvirtualcharactercdn.rongshuxia.com/2482F0C14C5823AB3F43424FFEEEFFA1.jpg")
        avatarList.add("https://xxvirtualcharactercdn.rongshuxia.com/2CD92938CCCDC972D4C3038217AD7FAF.jpg")

        chatThreeAvatarView = findViewById(R.id.chat_three_avatar_view)
        chatThreeAvatarView?.setAvatarList(avatarList)

        ScreenUtil.printDisplayMetricsInfo(this)

        binding.composeView.setContent {

            MaterialTheme {
                Surface {
                    composeButton()
                }
            }

        }
    }

    @Composable
    fun composeButton() {
        Button(onClick = {
            WidgetsActivity.launch(this)
        }) {
            Text(text = "测试Compose Widget")
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnCoordinateLayout -> {
                CoordinateLayoutActivity.launch(this)
            }

            R.id.btn_test_alarm_manager -> {
                AlarmManagerActivity.launch(this)
            }

            R.id.btnTestEditTextCustomAction -> {
                EditTextActivity.launch(this)
            }

            R.id.btn_test_slide_delete -> {
                TestDragDeleteItemActivity.launch(this)
            }

            R.id.btn_test_blur -> {
                TestBlurActivity.launch(this)
            }

            R.id.btnNinePatchTest -> {
                NinePatchDrawableActivity.launch(this)
            }

            R.id.btn_test_change_day_night_theme -> {
                ChangeDayNightThemeActivity.launch(this)
            }

            R.id.btn_test_merge -> {
                MergeTestActivity.launch(this)
            }

            R.id.btn_test_chip -> {
                MaterialChipActivity.launch(this)
            }

            R.id.btnTestActivityAnim -> {
                ActivityAnimTestActivity.launch(this)
            }

            R.id.btnTestSoftKeyboard -> {
                TestSoftKeyboardActivity.launch(this)
            }

            R.id.btnListenNestedScrollView -> {
                ListenNestedScrollViewActivity.launch(this)
            }

            R.id.btnTestRoundView -> {
                RoundScrollViewActivity.launch(this)
            }

            R.id.btnTestViewDrawOrder -> {
                MyViewGroupActivity.launch(this)
            }

            R.id.btnTestClipBoard -> {
                ClipBoardActivity.launch(this)
            }

            R.id.btn_load_res -> {
                LoadResActivity.launch(this)
            }

            R.id.btnTestVectorDrawable -> {
                VectorDrawableMainActivity.launch(this)
            }

            R.id.btn_draw_stage_red_packet -> {
                StageRedPacketActivity.launch(this)
            }

            R.id.btnTestSkeletonView -> {
                SkeletonViewActivity.launch(this)
            }

            R.id.btn_test_outline -> {
                OutlineTestActivity.launch(this)
            }

            R.id.btn_myruler_view -> {
                MyRuleViewActivity.launch(this)
            }

            R.id.btn_rule_view -> {
                RuleViewMainActivity.launch(this)
            }

            R.id.btn_ruler_view -> {
                RulerMainActivity.launch(this)
            }

            R.id.btn_ruler_view_money -> {
                MoneyScaleMainActivity.launch(this)
            }

            R.id.btn_round_progress_view -> {
                RoundProgressBarActivity.launch(this)
            }

            R.id.btnTestColorProgressBar -> {
                ColorfulProgressBarActivity.launch(this)
            }

            R.id.btnTestViewSwitcher -> {
                ViewSwitcherActivity.launch(this)
            }

            R.id.btnTestChildTabView -> {
                ChildTabTestActivity.launch(this)
            }

            R.id.btnTestPadding -> {
                PaddingTestActivity.launch(this)
            }

            R.id.btnTestCircleProgressView -> {
                RingProgressActivity.launch(this)
            }

            R.id.btnTestFontMetrics -> {
                FontMetricsActivity.launch(this)
            }

            R.id.btnTestFontFamily -> {
                FontFamilyMainActivity.launch(this)
            }

            R.id.btnTestRelativeRule -> {
                RelativeDynamicAddRuleActivity.launch(this)
            }

            R.id.btnTestScrollUp -> {
                TestScrollerUpViewActivity.launch(this)
            }

            R.id.btnTestSeekBar -> {
                SeekBarTestActivity.launch(this)
            }

            R.id.btnTestWaveView -> {
                TestWaveViewActivity.launch(this)
            }

            R.id.btnTestScrollFling -> {
                ScrollFlingTestActivity.launch(this)
            }

            R.id.btnCommonPopWindow -> {
                CommonPopWindowActivity.launch(this)
            }

            R.id.btnPickColor -> {
                ColorTestActivity.launch(this)
            }

            R.id.btnPalette -> {
                PaletteActivity.launch(this)
            }

            R.id.btnCanvasSaveRestore -> {

                CanvasSaveRestoreActivity.launch(this)
            }

            R.id.btnChangeImageColor -> {
                ChangeImageColorActivity.launch(this)
            }

            R.id.btnAutosizingTextView -> {
                AutoSizingTextViewActivity.launch(this)
            }

            R.id.btnTestTabLayout -> {
                TabLayoutActivity.launch(this)
            }

            R.id.btnListViewFloat -> {
                ListViewFloatActivity.launch(this)
            }

            R.id.btnListViewFloat2 -> {
                ListViewFloat2Activity.launch(this)
            }

            R.id.btn_test_lottie -> {
                LottieActivity.launch(this)
            }

            R.id.btnTextTicker -> {
                TestTickerActivity.launch(this)
            }

            R.id.btnRoundView -> {
                RoundViewActivity.launch(this)
            }

            R.id.btnTextSwitcher -> {
                TextSwitcherActivity.launch(this)
            }

            R.id.btnListView -> {
                ListViewActivity.launch(this)
            }

            R.id.btnTestCancelEVent -> {

                val assetManager = AssetManager::class.java.newInstance()
                TestCancelEventActivity.launch(this)
            }

            R.id.btnChangeCircleViewColor -> {
                if (Random.nextBoolean()) {
                    binding.circleView.setColor(R.color.colorPrimary)
                } else {
                    binding.circleView.setColor(R.color.colorAccent)
                }
            }

            R.id.btnNestedScroll -> {
                NestedScrollMainActivity.launch(this)
            }

            R.id.btnHandlerTest -> {
                HandlerActivity.launch(this)
            }

            R.id.btnNonMainThreadUpdateUITest -> {
                NonMainThreadUpdateUIActivity.launch(this)
            }

            R.id.btnDialogTest -> {
                DialogTestActivity.launch(this)
            }

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
                PopWindowWithInputActivity.launch(this)
            }

            R.id.btnCustomViewGetStart -> {
                //打开注释查看anr
                //Thread.sleep(10000)
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

            R.id.btnPinScrollText -> {
                PinScrollTextActivity.launch(this)
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
            R.id.btn_scrollview_recyclerview_activity -> ScrollViewNestRecyclerViewActivity.launch(
                this
            )

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
            R.id.btn_show_loading -> ProgressBarActivity.launch(this)
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

    fun launchMatrixTestActivity(view: View) {
        MatrixTestActivity.launch(this)
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied: ")
        Toast.makeText(this, "onPermissionsDenied", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
    }

}
