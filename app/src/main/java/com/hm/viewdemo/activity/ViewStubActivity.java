package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.hm.viewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试ViewStub的用法
 */
public class ViewStubActivity extends AppCompatActivity {


    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }*/



    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.btn_more)
    Button btnMore;
    ViewStub viewStub;
    @BindView(R.id.btn_less)
    Button btnLess;
    private EditText editExtra1;
    private EditText editExtra2;
    private EditText editExtra3;
    private View inflatedView;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ViewStubActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viswstub);



        ButterKnife.bind(this);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inflatedView == null) {
                    viewStub = (ViewStub) findViewById(R.id.view_stub);
                    if (viewStub != null) {
                        inflatedView = viewStub.inflate();
                        editExtra1 = (EditText) inflatedView.findViewById(R.id.edit_extra1);
                        editExtra2 = (EditText) inflatedView.findViewById(R.id.edit_extra2);
                        editExtra3 = (EditText) inflatedView.findViewById(R.id.edit_extra3);
                    }
                } else {
                    inflatedView.setVisibility(View.VISIBLE);
                }
            }
        });
        btnLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inflatedView != null) {
                    inflatedView.setVisibility(View.GONE);
                }
            }
        });
    }
}
