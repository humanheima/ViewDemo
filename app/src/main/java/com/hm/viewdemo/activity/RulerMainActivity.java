package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;

import cn.wang.ruler.RulerActivity;

/**
 * Created by dumingwei on 2021/7/26
 * <p>
 * Desc:
 */

public class RulerMainActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent starter = new Intent(context, RulerMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulerMainActivity.this, RulerActivity.class);
                startActivity(intent);
            }
        });
    }
}
