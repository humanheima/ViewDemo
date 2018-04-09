package com.hm.viewdemo.activity;

import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btn_time_selector, R.id.btn_about_handler, R.id.btn_view_stub, R.id.btn_horizontal_vertical_conflict, R.id.btn_customer_view, R.id.btn_expandable_listview,
            R.id.btn_vertical_verticla_activity, R.id.btn_scrollview_recyclerview_activity,
            R.id.btn_MaxHeightLayoutActivity, R.id.btn_bottomSheet, R.id.btn_constraint_layout,
            R.id.btn_autowrap_textview, R.id.btn_drag_slop_layout, R.id.btn_view_drag_helper,
            R.id.btn_pullrefresh, R.id.btn_textview, R.id.btn_histogram_view, R.id.btn_test_fudan,
            R.id.btn_show_loading, R.id.btn_event_dispatch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_time_selector:
                TimeSelectorActivity.launch(this);
                break;
            case R.id.btn_about_handler:
                TestHandlerAsyncTaskActivity.launch(this);
                break;
            case R.id.btn_view_stub:
                ViewStubActivity.launch(this);
                break;
            case R.id.btn_horizontal_vertical_conflict:
                HorizontalVerticalConflictActivity.launch(this);
                break;
            case R.id.btn_customer_view:
                CustomerViewActivity.launch(this);
                break;
            case R.id.btn_expandable_listview:
                ExpandableListViewActivity.launch(this);
                break;
            case R.id.btn_vertical_verticla_activity:
                VerticalVerticalActivity.launch(this);
                break;
            case R.id.btn_scrollview_recyclerview_activity:
                ScrollViewNestRecyclerViewActivity.launch(this);
                break;
            case R.id.btn_MaxHeightLayoutActivity:
                MaxHeightLayoutActivity.launch(this);
                break;
            case R.id.btn_bottomSheet:
                BottomSheetActivity.launch(this);
                break;
            case R.id.btn_constraint_layout:
                ConstraintActivity.launch(this);
                break;
            case R.id.btn_autowrap_textview:
                AutoWrapTextViewActivity.launch(this);
                break;
            case R.id.btn_drag_slop_layout:
                DragSlopLayoutActivity.launch(this);
                break;
            case R.id.btn_view_drag_helper:
                ViewDragHelperActivity.launch(this);
                break;
            case R.id.btn_pullrefresh:
                PullToRefreshActivity.launch(this);
                break;
            case R.id.btn_textview:
                TextViewActivity.launch(this);
                break;
            case R.id.btn_histogram_view:
                MpAndroidChartActivity.launch(this);
                break;
            case R.id.btn_test_fudan:
                TestFuDanActivity.launch(this);
                break;
            case R.id.btn_show_loading:
                LoadingDialogActivity.launch(this);
                break;
            case R.id.btn_event_dispatch:
                EventDispatchActivity.launch(this);
                break;
            default:
                break;
        }

    }

    public void launchRoundImageViewActivity(View view) {
        RoundImageViewActivity.launch(this);
    }

    public void launchRoundImageViewXfermodeActivity(View view) {
        RoundImageViewXfermodeActivity.launch(this);
    }

    public void launchXfermodeTestActivity(View view) {
        XfermodeTestActivity.launch(this);
    }

    public void launchDrawEveryThingActivity(View view) {
        DrawEveryThingActivity.launch(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
