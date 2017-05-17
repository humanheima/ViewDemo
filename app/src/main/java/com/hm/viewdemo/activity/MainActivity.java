package com.hm.viewdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hm.viewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_horizontal_vertical_conflict)
    Button btnHorizontalVerticalConflict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_horizontal_vertical_conflict, R.id.btn_customer_view, R.id.btn_expandable_listview,
            R.id.btn_vertical_verticla_activity, R.id.btn_scrollview_recyclerview_activity,
            R.id.btn_MaxHeightLayoutActivity, R.id.btn_bottomSheet, R.id.btn_constraint_layout, R.id.btn_autowrap_textview})
    public void onClick(View view) {
        switch (view.getId()) {
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
            default:
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
