package com.hm.viewdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dumingwei on 2021/3/15.
 * <p>
 * Desc:
 */
public class SpUtil {

    private static SpUtil spUtil;

    private SharedPreferences sp;

    private SpUtil(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        }
    }

    public static synchronized SpUtil getInstance(Context context) {
        if (spUtil == null) {
            spUtil = new SpUtil(context);
        }
        return spUtil;
    }

    public void saveBoolean(String key, boolean value) {
        if (sp != null) {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    public boolean getBoolean(String key) {
        if (sp != null) {
            return sp.getBoolean(key, false);
        }
        return false;
    }

    public void saveInt(String key, int value) {
        if (sp != null) {
            sp.edit().putInt(key, value).apply();
        }
    }

    public int getInt(String key) {
        if (sp != null) {
            return sp.getInt(key, 0);
        }
        return -1;
    }

}
