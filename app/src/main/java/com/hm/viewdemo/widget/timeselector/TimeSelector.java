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

/**
 * Created by liuli on 2015/11/27.
 */
public class TimeSelector {

    public static final String YMD_FORMAT = "yyyy-MM-dd";
    public static final String YMD_HS_FORMAT = "yyyy-MM-dd HH:mm";
    private final String TAG = getClass().getSimpleName();
    private final int MAX_MINUTE = 59;
    private final int MAX_MONTH = 12;
    private final long ANIMATOR_DELAY = 200L;
    private final long CHANGE_DELAY = 90L;
    private Calendar currentCalender;
    private int currentYear, currentMonth, currentDay;
    private int currentHour;//24小时制
    private int currentMinute;
    private int scrollUnits = SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value;
    private ResultHandler handler;
    private Context context;
    private Dialog seletorDialog;
    private PickerView pvYear;
    private PickerView pvMonth;
    private PickerView pvDay;
    private PickerView pvHour;
    private PickerView pvMinute;
    private int MAX_HOUR = 23;
    private ArrayList<String> year, month, day, hour, minute;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private TextView tvSelect, tvCancel;
    //是否是年月日类型
    private boolean typeYmd;
    private String dateFormat;

    public TimeSelector(Context context, ResultHandler resultHandler, Calendar startCalendar,
                        Calendar endCalendar, String dateFormat) {
        this.context = context;
        this.handler = resultHandler;
        this.startCalendar = startCalendar;
        this.endCalendar = endCalendar;
        currentCalender = Calendar.getInstance();
        typeYmd = YMD_FORMAT.equals(dateFormat);
        this.dateFormat = dateFormat;
        initParameter();
        initDialog();
        initView();
    }

    /**
     * 开始和结束日期指定以后，不再变化
     */
    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);

        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);

        spanYear = startYear != endYear;//起始日期和结束日期不是同一年
        spanMon = (!spanYear) && (startMonth != endMonth);//起始日期和结束日期是同一年，但是月份不一样
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);

        Log.d(TAG, "initParameter: spanYear=" + spanYear + ",spanMon=" + spanMon + ",spanDay" + spanDay
                + ",spanHour=" + spanHour + ",spanMin=" + spanMin);
    }

    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(context, R.style.time_dialog);
            seletorDialog.setCancelable(false);
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (typeYmd) {
                seletorDialog.setContentView(R.layout.time_selector_ymd);
            } else {
                seletorDialog.setContentView(R.layout.time_selector_ymd_hs);
            }
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getInstance(context).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        if (typeYmd) {
            pvYear = seletorDialog.findViewById(R.id.year_pv);
            pvMonth = seletorDialog.findViewById(R.id.month_pv);
            pvDay = seletorDialog.findViewById(R.id.day_pv);
            tvCancel = seletorDialog.findViewById(R.id.tv_cancel);
            tvSelect = seletorDialog.findViewById(R.id.tv_select);
        } else {
            pvYear = seletorDialog.findViewById(R.id.year_pv);
            pvMonth = seletorDialog.findViewById(R.id.month_pv);
            pvDay = seletorDialog.findViewById(R.id.day_pv);
            pvHour = seletorDialog.findViewById(R.id.hour_pv);
            pvMinute = seletorDialog.findViewById(R.id.minute_pv);

            tvCancel = seletorDialog.findViewById(R.id.tv_cancel);
            tvSelect = seletorDialog.findViewById(R.id.tv_select);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletorDialog.dismiss();
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeYmd) {
                    handler.handle(format(currentCalender.getTime(), YMD_FORMAT));
                } else {
                    handler.handle(format(currentCalender.getTime(), YMD_HS_FORMAT));
                }
                seletorDialog.dismiss();
            }
        });
        addListener();
    }

    private String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public void show(String currentDate) {
        Log.d(TAG, "当前时间: " + currentDate);
        if (!TextUtils.isEmpty(currentDate)) {
            currentCalender.setTime(DateUtil.parse(currentDate, dateFormat));
        } else {
            currentCalender.setTimeInMillis(System.currentTimeMillis());
        }
        currentYear = currentCalender.get(Calendar.YEAR);
        currentMonth = currentCalender.get(Calendar.MONTH) + 1;
        currentDay = currentCalender.get(Calendar.DAY_OF_MONTH);
        if (!typeYmd) {
            currentHour = currentCalender.get(Calendar.HOUR_OF_DAY);
            currentMinute = currentCalender.get(Calendar.MINUTE);
        }
        Log.d(TAG, "show: currentHour=" + currentHour + ",currentMinute=" + currentMinute);
        initTimer();
        seletorDialog.show();
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
            for (int i = 0; i <= MAX_HOUR; i++) {
                hour.add(formatTimeUnit(i));
            }
            for (int i = 0; i <= MAX_MINUTE; i++) {
                minute.add(formatTimeUnit(i));
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));
            hour.add(formatTimeUnit(startHour));
            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        }
        loadComponent();
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
    }

    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void loadComponent() {
        pvYear.setData(year);
        pvMonth.setData(month);
        pvDay.setData(day);
        if (!typeYmd) {
            pvHour.setData(hour);
            pvMinute.setData(minute);
        }
        pvYear.setSelected(year.indexOf(String.valueOf(currentYear)));
        pvMonth.setSelected(month.indexOf(formatTimeUnit(currentMonth)));
        pvDay.setSelected(day.indexOf(formatTimeUnit(currentDay)));
        if (!typeYmd) {
            pvHour.setSelected(hour.indexOf(formatTimeUnit(currentHour)));
            pvMinute.setSelected(minute.indexOf(formatTimeUnit(currentMinute)));
        }
        executeScroll();
    }

    private void executeScroll() {
        pvYear.setCanScroll(year.size() > 1);
        pvMonth.setCanScroll(month.size() > 1);
        pvDay.setCanScroll(day.size() > 1);
        if (!typeYmd) {
            pvHour.setCanScroll(hour.size() > 1);
            pvMinute.setCanScroll(minute.size() > 1);
        }
    }

    private void addListener() {
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
                if (!typeYmd) {
                    hourChange();
                }
            }
        });
        if (pvHour != null) {
            pvHour.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    currentCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                    minuteChange();
                }
            });
        }
        if (pvMinute != null) {
            pvMinute.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    currentCalender.set(Calendar.MINUTE, Integer.parseInt(text));
                }
            });
        }
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
        if (!typeYmd) {
            pvDay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hourChange();
                }
            }, CHANGE_DELAY);
        }
    }

    private void hourChange() {
        hour.clear();
        for (int i = 0; i <= MAX_HOUR; i++) {
            hour.add(formatTimeUnit(i));
        }
        currentCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
        pvHour.setData(hour);
        pvHour.setSelected(0);
        executeAnimator(ANIMATOR_DELAY, pvHour);
        pvHour.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, CHANGE_DELAY);
    }

    private void minuteChange() {
        minute.clear();
        for (int i = 0; i <= MAX_MINUTE; i++) {
            minute.add(formatTimeUnit(i));
        }
        currentCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
        pvMinute.setData(minute);
        pvMinute.setSelected(0);
        executeAnimator(ANIMATOR_DELAY, pvMinute);
        executeScroll();
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
        if (null != pvHour) {
            pvHour.setIsLoop(isLoop);
        }
        if (null != pvMinute) {
            pvMinute.setIsLoop(isLoop);
        }
    }

    public enum SCROLLTYPE {

        HOUR(1),
        MINUTE(2);

        public int value;

        SCROLLTYPE(int value) {
            this.value = value;
        }
    }

    public interface ResultHandler {
        void handle(String time);
    }
}
