package com.hjc.util;

import android.app.AlarmManager;
import android.content.Context;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hujiachun on 16/1/4.
 */
public class TimeUtil {

    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.CHINESE);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }

    public static String getTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINESE);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }

    public static String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }

    public static Date getMeizuTime() throws IOException {
        URL url = new URL("http://www.meizu.com");
        URLConnection uc = url.openConnection();
        uc.connect();
        return new Date(uc.getDate());
    }

    public static void setSystemTime(Context context, Date date){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setTime(date.getTime());
    }

    public static void setSystemTime(Context context, long time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setTime(time);
    }

    public static void restoreSystemTime(Context context){
        try {
            setSystemTime(context, getMeizuTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
