package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.viewdemo.R;

public class TestGetDrawingCachingActivity extends AppCompatActivity {


    private final String TAG = "TestGetDrawingCachingAc";

    //布局文件对应的view
    private View view;
    private TextView tvNumber;
    private int number = 0;

    //用来显示生成的bitmap
    private ImageView ivTop;
    private Button btnGetBitmap;


    public static void launch(Context context) {
        Intent starter = new Intent(context, TestGetDrawingCachingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_get_drawing_caching);
        //view = LayoutInflater.from(this).inflate(R.layout.layout_drawing_cache, null);
        tvNumber = findViewById(R.id.tvNumber);

        ivTop = findViewById(R.id.ivTop);
        btnGetBitmap = findViewById(R.id.btnGetBitmap);
        btnGetBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number++;
                //每次生成bitmap之前改变一下tvNumber的text
                tvNumber.setText(String.valueOf(number));
                Bitmap bitmap = convertViewToBitmap2(tvNumber);
                ivTop.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        });
    }

    /**
     * 通过drawingCache获取bitmap
     *
     * @param view
     * @return
     */
    private Bitmap convertViewToBitmap2(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //如果不调用这个方法，每次生成的bitmap相同
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 通过canvas复制view的bitmap
     *
     * @param view
     * @return
     */
    private Bitmap copyByCanvas2(View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Log.d(TAG, "copyByCanvas: width=" + width + ",height=" + height);
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bp);
        view.draw(canvas);
        canvas.save();
        return bp;
    }

    /**
     * 通过drawingCache获取bitmap
     *
     * @param view
     * @return
     */
    private Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        //Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        Bitmap bitmap = view.getDrawingCache();
        //如果不调用这个方法，每次生成的bitmap相同
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 通过canvas复制view的bitmap
     *
     * @param view
     * @return
     */
    private Bitmap copyByCanvas(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Log.d(TAG, "copyByCanvas: width=" + width + ",height=" + height);
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bp);
        view.draw(canvas);
        canvas.save();
        return bp;
    }
}
