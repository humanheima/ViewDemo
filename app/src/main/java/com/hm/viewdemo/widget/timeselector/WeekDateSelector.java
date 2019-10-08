package com.hm.viewdemo.widget.timeselector;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hm.viewdemo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Crete by dumingwei on 2019-08-22
 * Desc: 第一天也应该选择日期，而不是动态计算
 */
public class WeekDateSelector {

    private static final long ANIMATOR_DELAY = 200L;
    private final String TAG = getClass().getSimpleName();


    private ResultHandler handler;
    private Context context;
    private Dialog selectorDialog;

    private PickerView pvDate;
    private PickerView pvTimePeriod;

    private ArrayList<String> date;
    private ArrayList<String> timePeriod;

    private Calendar startCalendar;
    private TextView tvConfirm;

    private int startHour;
    private int endHour;

    private DateFormat dateFormat;
    private static final String today = "今天";

    private String selectedDate;
    private String selectedTimePeriod;


    public WeekDateSelector(Context context, ResultHandler resultHandler, int startHour,
                            int endHour, Calendar startCalendar, String formatPattern) {
        this.context = context;
        this.handler = resultHandler;
        this.startCalendar = startCalendar;
        this.startHour = startHour;
        this.endHour = endHour;
        dateFormat = new SimpleDateFormat(formatPattern, Locale.getDefault());
        initDialog();
        initView();
    }

    private void initDialog() {
        if (selectorDialog == null) {
            selectorDialog = new Dialog(context, R.style.time_dialog);
            selectorDialog.setCancelable(false);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setContentView(R.layout.time_selector_week);

            Window window = selectorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getInstance(context).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        pvDate = selectorDialog.findViewById(R.id.pvDate);
        pvTimePeriod = selectorDialog.findViewById(R.id.pvTimePeriod);

        tvConfirm = selectorDialog.findViewById(R.id.tvConfirm);

        addListener();
    }

    private void addListener() {

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(selectedDate + selectedTimePeriod);
                selectorDialog.dismiss();
            }
        });
        pvDate.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedDate = text;
                timeChange(text);
            }
        });
        pvTimePeriod.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedTimePeriod = text;
            }
        });
    }

    public void show() {
        initTimer();
        selectorDialog.show();
    }

    private void initTimer() {
        initArrayList();
        //如果当前时间已经 >= (endHour - 2)说明今天就不可预约了，就不添加今天
        boolean addToday = false;

        int currentHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        if (currentHour <= (endHour - 2)) {
            date.add(today);
            addToday = true;
        }

        for (int i = 0; i < 7; i++) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            date.add(dateFormat.format(startCalendar.getTime()));
        }

        //初始化的时候默认选择第一个
        selectedDate = date.get(0);

        getTimePeriods(addToday);

        startCalendar.setTimeInMillis(System.currentTimeMillis());

        loadComponent();
    }

    private void getTimePeriods(boolean today) {
        int currentHour = startHour;
        if (today) {
            currentHour = startCalendar.get(Calendar.HOUR_OF_DAY);
            if (currentHour < startHour) {
                currentHour = startHour;
            }
        }
        for (; currentHour + 2 < endHour; currentHour += 2) {
            String period = String.format("%1$d:00-%2$d:00", currentHour, currentHour + 2);
            Log.d(TAG, "initTimer: " + period);
            timePeriod.add(period);
        }

        if (currentHour < endHour) {
            String period = String.format("%1$d:00-%2$d:00", currentHour, endHour);
            Log.d(TAG, "initTimer: " + period);
            timePeriod.add(period);
        }

        //初始化的时候默认选择第一个
        selectedTimePeriod = timePeriod.get(0);
    }

    private void initArrayList() {
        if (date == null) {
            date = new ArrayList<>();
        }
        if (timePeriod == null) {
            timePeriod = new ArrayList<>();
        }
        date.clear();
        timePeriod.clear();
    }

    private void loadComponent() {
        pvDate.setData(date);
        pvTimePeriod.setData(timePeriod);
        pvDate.setSelected(0);
        pvTimePeriod.setSelected(0);
        executeScroll();
    }

    private void executeScroll() {
        pvDate.setCanScroll(date.size() > 1);
        pvTimePeriod.setCanScroll(timePeriod.size() > 1);
    }

    private void timeChange(String text) {
        timePeriod.clear();
        if (today.equals(text)) {
            getTimePeriods(true);
        } else {
            getTimePeriods(false);
        }
        pvTimePeriod.setData(timePeriod);
        pvTimePeriod.setSelected(0);
        executeScroll();
        executeAnimator(ANIMATOR_DELAY, pvTimePeriod);
    }

    private void executeAnimator(long duration, View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(duration).start();
    }

    public void setNextBtTip(String str) {
        tvConfirm.setText(str);
    }

    public void setIsLoop(boolean isLoop) {
        pvDate.setIsLoop(isLoop);
        pvTimePeriod.setIsLoop(isLoop);
    }

    public interface ResultHandler {
        void handle(String time);
    }
}
