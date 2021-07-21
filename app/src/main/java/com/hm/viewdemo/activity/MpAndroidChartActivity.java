package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.HistogramView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用MPAndroidChart
 * https://github.com/PhilJay/MPAndroidChart
 */
public class MpAndroidChartActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.histogram_view_1)
    HistogramView histogramView1;
    @BindView(R.id.histogram_view_2)
    HistogramView histogramView2;
    @BindView(R.id.histogram_view_3)
    HistogramView histogramView3;
    @BindView(R.id.histogram_view_4)
    HistogramView histogramView4;

    public static void launch(Context context) {
        Intent starter = new Intent(context, MpAndroidChartActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_android_chart);
        ButterKnife.bind(this);
        Log.e(TAG, "200 / 1000f=" + 200 / 1000f);
        histogramView1.setForgroundHeight(200 / 1000f);
        histogramView2.setForgroundHeight(400 / 1000f);
        histogramView3.setForgroundHeight(1000 / 1000f);
        histogramView4.setForgroundHeight(0 / 1000f);
    }
}
