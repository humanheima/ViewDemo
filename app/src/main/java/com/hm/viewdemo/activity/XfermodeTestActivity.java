package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

/**
 * Crete by dumingwei on 2019-08-14
 * Desc:
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
