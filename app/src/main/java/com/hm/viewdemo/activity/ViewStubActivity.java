package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
