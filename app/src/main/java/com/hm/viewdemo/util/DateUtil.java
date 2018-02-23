package com.hm.viewdemo.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by dumingwei on 2018/2/22 0022.
 */

public class DateUtil {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public static String currentTime() {
        return df.format(System.currentTimeMillis());
    }
}
