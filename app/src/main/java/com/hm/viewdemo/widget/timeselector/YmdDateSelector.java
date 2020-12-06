package com.hm.viewdemo.widget.timeselector;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hm.viewdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liuli on 2015/11/27.
 */
public class YmdDateSelector {

    public static final String YMD_FORMAT = "yyyy-MM-dd";

    private static final int MAX_MONTH = 12;
    private static final long ANIMATOR_DELAY = 200L;
    private static final long CHANGE_DELAY = 90L;

    private final String TAG = getClass().getSimpleName();

    private Calendar currentCalender;

    private int currentYear;
    private int currentMonth;
    private int currentDay;

    private ResultHandler handler;
    private Context context;
    private Dialog selectorDialog;

    private PickerView pvYear;
    private PickerView pvMonth;
    private PickerView pvDay;

    private ArrayList<String> year;
    private ArrayList<String> month;
    private ArrayList<String> day;

    private int startYear;
    private int startMonth;
    private int startDay;

    private int endYear;
    private int endMonth;
    private int endDay;

    private boolean spanYear;
    private boolean spanMon;
    private boolean spanDay;

    private Calendar startCalendar;
    private Calendar endCalendar;
    private TextView tvSelect;
    private TextView tvCancel;
    private String dateFormat;

    public YmdDateSelector(Context context, ResultHandler resultHandler, Calendar startCalendar,
                           Calendar endCalendar, String dateFormat) {
        this.context = context;
        this.handler = resultHandler;
        this.startCalendar = startCalendar;
        this.endCalendar = endCalendar;
        currentCalender = Calendar.getInstance();
        this.dateFormat = dateFormat;
        initStartEnd();
        initDialog();
        initView();
    }

    /**
     * 开始和结束日期指定以后，不再变化
     */
    private void initStartEnd() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        spanYear = startYear != endYear;//起始日期和结束日期不是同一年
        spanMon = (!spanYear) && (startMonth != endMonth);//起始日期和结束日期是同一年，但是月份不一样
        spanDay = (!spanMon) && (startDay != endDay);

        Log.i(TAG, "initStartEnd: spanYear=" + spanYear + ",spanMon=" + spanMon + ",spanDay" + spanDay);
    }

    private void initDialog() {
        if (selectorDialog == null) {
            selectorDialog = new Dialog(context, R.style.time_dialog);
            selectorDialog.setCancelable(false);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setContentView(R.layout.time_selector_ymd);

            Window window = selectorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getInstance(context).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        pvYear = selectorDialog.findViewById(R.id.year_pv);
        pvMonth = selectorDialog.findViewById(R.id.month_pv);
        pvDay = selectorDialog.findViewById(R.id.day_pv);
        tvCancel = selectorDialog.findViewById(R.id.tv_cancel);
        tvSelect = selectorDialog.findViewById(R.id.tv_select);
        addListener();
    }

    private void addListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorDialog.dismiss();
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(format(currentCalender.getTime(), YMD_FORMAT));
                selectorDialog.dismiss();
            }
        });
        pvYear.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currentCalender.set(Calendar.YEAR, Integer.parseInt(text));
                monthChange();
            }
        });
        pvMonth.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currentCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                dayChange();
            }
        });
        pvDay.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                currentCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
            }
        });
    }

    private String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public void show(String currentDate) {
        Log.i(TAG, "当前时间: " + currentDate);
        if (!TextUtils.isEmpty(currentDate)) {
            currentCalender.setTime(parse(currentDate, dateFormat));
        } else {
            currentCalender.setTimeInMillis(System.currentTimeMillis());
        }
        currentYear = currentCalender.get(Calendar.YEAR);
        currentMonth = currentCalender.get(Calendar.MONTH) + 1;
        currentDay = currentCalender.get(Calendar.DAY_OF_MONTH);
        initTimer();
        selectorDialog.show();
    }

    public static Date parse(String strDate, String pattern) {
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            if (currentYear == startYear) {
                //如果当前年份和起始年份是同一年，月份必须大于等于起始月份
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            } else if (currentYear == endYear) {
                //如果当前年份和起始年份是同一年，月份必须小于等于结束月份
                for (int i = 1; i <= endMonth; i++) {
                    month.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 1; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            }
            if (currentYear == startYear && currentMonth == startMonth) {
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            } else if (currentYear == endYear && currentMonth == endMonth) {
                for (int i = 1; i <= endDay; i++) {
                    day.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 1; i <= currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        }
        loadComponent();
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
    }

    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + unit : String.valueOf(unit);
    }

    private void loadComponent() {
        pvYear.setData(year);
        pvMonth.setData(month);
        pvDay.setData(day);
        pvYear.setSelected(year.indexOf(String.valueOf(currentYear)));
        pvMonth.setSelected(month.indexOf(formatTimeUnit(currentMonth)));
        pvDay.setSelected(day.indexOf(formatTimeUnit(currentDay)));
        executeScroll();
    }

    private void executeScroll() {
        pvYear.setCanScroll(year.size() > 1);
        pvMonth.setCanScroll(month.size() > 1);
        pvDay.setCanScroll(day.size() > 1);
    }

    private void monthChange() {
        month.clear();
        int selectedYear = currentCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        }
        currentCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        pvMonth.setData(month);
        pvMonth.setSelected(0);
        executeAnimator(ANIMATOR_DELAY, pvMonth);
        pvMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, CHANGE_DELAY);
    }

    private void dayChange() {
        day.clear();
        int selectedYear = currentCalender.get(Calendar.YEAR);
        int selectedMonth = currentCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        }
        currentCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
        pvDay.setData(day);
        pvDay.setSelected(0);
        executeAnimator(ANIMATOR_DELAY, pvDay);
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
        tvSelect.setText(str);
    }

    public void setIsLoop(boolean isLoop) {
        pvYear.setIsLoop(isLoop);
        pvMonth.setIsLoop(isLoop);
        pvDay.setIsLoop(isLoop);
    }

    public interface ResultHandler {
        void handle(String time);
    }
}
