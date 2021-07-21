package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;

/**
 * Crete by dumingwei on 2019-08-14
 * Desc:
 * 参考 https://blog.csdn.net/cquwentao/article/details/51423371
 *     https://blog.csdn.net/iispring/article/details/50472485#commentsedit 这篇文章中有错误，看leilifengxingmw的评论
 *
 *
 */
public class XfermodeTestActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, XfermodeTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfermode_test);
    }

}
