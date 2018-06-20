package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityTimeSelectorBinding;
import com.hm.viewdemo.widget.timeselector.TimeSelector;

import java.util.Calendar;

/**
 * 测试TimeSelector
 */
public class TimeSelectorActivity extends AppCompatActivity {

    private TimeSelector ymdTimeSelector;
    private TimeSelector ymdmsTimeSelector;
    private TextView tvYmd;
    private TextView tvYmdms;
    private ActivityTimeSelectorBinding binding;
    private Calendar startCalendar;
    private Calendar endCalendar;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TimeSelectorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_selector);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.YEAR, 20);

        ymdTimeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tvYmd.setText(time);
            }
        }, startCalendar, endCalendar, TimeSelector.YMD_HS_FORMAT);
        ymdTimeSelector.setIsLoop(false);

        ymdmsTimeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tvYmdms.setText(time);
            }
        }, startCalendar, endCalendar, TimeSelector.YMD_FORMAT);
        ymdmsTimeSelector.setIsLoop(false);
        tvYmd = binding.tvYmd;
        tvYmdms = binding.tvYmdMs;
    }

    public void showYmdTimeSelector(View view) {
        String time = tvYmd.getText().toString();
        if (TextUtils.isEmpty(time)) {
            ymdTimeSelector.show(null);
        } else {
            ymdTimeSelector.show(time);
        }
    }

    public void showYmdmsTimeSelector(View view) {
        String time = tvYmdms.getText().toString();
        if (TextUtils.isEmpty(time)) {
            ymdmsTimeSelector.show(null);
        } else {
            ymdmsTimeSelector.show(time);
        }
    }
}
