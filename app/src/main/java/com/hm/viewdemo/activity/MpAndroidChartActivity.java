package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityMpAndroidChartBinding;

/**
 * 使用MPAndroidChart
 * https://github.com/PhilJay/MPAndroidChart
 */
public class MpAndroidChartActivity extends BaseActivity<ActivityMpAndroidChartBinding> {

    private final String TAG = getClass().getSimpleName();

    public static void launch(Context context) {
        Intent starter = new Intent(context, MpAndroidChartActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityMpAndroidChartBinding createViewBinding() {
        return ActivityMpAndroidChartBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        Log.e(TAG, "200 / 1000f=" + 200 / 1000f);
        binding.histogramView1.setForgroundHeight(200 / 1000f);
        binding.histogramView2.setForgroundHeight(400 / 1000f);
        binding.histogramView3.setForgroundHeight(1000 / 1000f);
        binding.histogramView4.setForgroundHeight(0 / 1000f);

    }
}
