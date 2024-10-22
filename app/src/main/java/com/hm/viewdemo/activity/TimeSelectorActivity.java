package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;
import com.hm.viewdemo.databinding.ActivityTimeSelectorBinding;
import com.hm.viewdemo.widget.timeselector.WeekDateSelector;
import com.hm.viewdemo.widget.timeselector.YmdDateSelector;
import com.hm.viewdemo.widget.timeselector.YmdhmDateSelector;

import java.util.Calendar;

/**
 * 测试TimeSelector
 */
public class TimeSelectorActivity extends AppCompatActivity {

    private static final String TAG = "TimeSelectorActivity";

    private YmdDateSelector ymdTimeSelector;
    private YmdhmDateSelector ymdmsTimeSelector;

    private WeekDateSelector weekDateSelector;

    private TextView tvYmd;
    private TextView tvYmdms;
    private TextView tvWeek;
    private ActivityTimeSelectorBinding binding;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private String pattern;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TimeSelectorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeSelectorBinding.bind(getLayoutInflater().inflate(R.layout.activity_time_selector, null));
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.YEAR, 20);

        ymdTimeSelector = new YmdDateSelector(this, new YmdDateSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tvYmd.setText(time);
            }
        }, startCalendar, endCalendar, YmdDateSelector.YMD_FORMAT);
        ymdTimeSelector.setIsLoop(false);

        ymdmsTimeSelector = new YmdhmDateSelector(this, new YmdhmDateSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tvYmdms.setText(time);
            }
        }, startCalendar, endCalendar, YmdhmDateSelector.YMD_HS_FORMAT);
        ymdmsTimeSelector.setIsLoop(false);
        tvYmd = binding.tvYmd;
        tvYmdms = binding.tvYmdMs;
        tvWeek = binding.tvWeek;

        //pattern = "M月d日EEEE";
        pattern = "M月d日E";

        weekDateSelector = new WeekDateSelector(this, new WeekDateSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                Log.i(TAG, "handle: " + time);
                tvWeek.setText(time);
            }
        }, 9, 21, startCalendar, pattern);

        weekDateSelector.setIsLoop(false);
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

    public void showWeekTimeSelector(View view) {

        weekDateSelector.show();
    }
}
