package com.hm.viewdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hm.viewdemo.R;
import com.hm.viewdemo.activity.CustomerViewActivity;
import com.hm.viewdemo.activity.HorizontalVerticalConflictActivity;

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

    @OnClick({R.id.btn_horizontal_vertical_conflict, R.id.btn_customer_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_horizontal_vertical_conflict:
                HorizontalVerticalConflictActivity.launch(this);
                break;
            case R.id.btn_customer_view:
                CustomerViewActivity.launch(this);
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
