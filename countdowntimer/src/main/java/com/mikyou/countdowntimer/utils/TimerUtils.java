package com.mikyou.countdowntimer.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.mikyou.countdowntimer.bean.JDCountDownTimer;
import com.mikyou.countdowntimer.bean.MikyouCountDownTimer;
import com.mikyou.countdowntimer.bean.VIPCountDownTimer;

/**
 * Created by mikyou on 16-10-22.
 */
public class TimerUtils {
    public static final int JD_STYLE = 0;
    public static final int VIP_STYLE = 1;
    public static final int DEFAULT_STYLE = 3;

    public static final String TIME_STYLE_ONE = "HH:mm:ss";
    public static final String TIME_STYLE_TWO = "HH时mm分ss秒";
    public static final String TIME_STYLE_THREE = "dd天HH时mm分ss秒";
    public static final String TIME_STYLE_FOUR = "dd天HH时mm分";

    public static MikyouCountDownTimer getTimer(int style,Context mContext, long mGapTime, String mTimePattern, int mDrawableId){
        MikyouCountDownTimer mCountDownTimer = null;
        switch (style){
            case JD_STYLE:
                mCountDownTimer = new JDCountDownTimer(mContext,mGapTime,mTimePattern,mDrawableId);
                break;
            case VIP_STYLE:
                mCountDownTimer = new VIPCountDownTimer(mContext,mGapTime,mTimePattern,mDrawableId);
                break;
            case DEFAULT_STYLE:
                mCountDownTimer = new MikyouCountDownTimer(mContext,mGapTime,mTimePattern,mDrawableId);
                break;
        }
        return mCountDownTimer;
    }
//得到倒计时字符串中的数值块部分
    public static String[] getNumInTimerStr(String mTimerStr){
        return mTimerStr.split("[^\\d]");
    }
    //得到倒计时中字符串中的非数值的字符串,并把数值过滤掉重新组合成一个字符串，并把字符串拆分字符数组，也就是保存倒计时中间的间隔
    public static char[] getNonNumInTimerStr(String mTimerStr){
        return mTimerStr.replaceAll("\\d","").toCharArray();
    }
   //设置字体颜色
    public static ForegroundColorSpan getTextColorSpan(String color){
        ForegroundColorSpan mSpan = null;
        if (mSpan == null){
            mSpan = new ForegroundColorSpan(Color.parseColor(color));
        }
        return mSpan;
    }
    //设置内容的Span
    public static void setContentSpan(SpannableString mSpan, Object span, int start,
                               int end) {
        mSpan.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
