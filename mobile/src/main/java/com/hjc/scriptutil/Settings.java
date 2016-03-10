package com.hjc.scriptutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hujiachun on 16/2/25.
 */
public class Settings {

    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_PERFORMANCE = "performance";
    public static final String KEY_LOGCAT_PATH = "logcat_path";
    public static final String KEY_LOGCAT_STATE = "logcat_state";
    public static final String KEY_UIAUTOMATOR = "ui_resultpath";
    public static final String KEY_HEAP_STATE = "root_state";
    public static final String KEY_PACKAGE = "package_name";
    public static final String KEY_CASE = "test_case";
    public static final String KEY_TIME = "time_str";

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
