package com.hjc.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;


import com.hjc.scriptutil.Settings;
import com.hjc.scriptutil.matcher.ScreenUtil;
import com.hjc.util.ShellUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by hujiachun on 16/1/11.
 */
public class Screenshot extends BroadcastReceiver{
    String screenshotFile, name, ta_name;
    boolean result;
    SharedPreferences logcat_sp;


    @Override
    public void onReceive(final Context context, Intent intent) {
        screenshotFile = intent.getStringExtra("screenshotFile");
        name = intent.getStringExtra("name");
        result = intent.getBooleanExtra("result", false);

        if(screenshotFile != null){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    File picfilefi = new File(screenshotFile + "/" + name + ".png");
                    if(picfilefi.exists()){
                        picfilefi.delete();
                    }

                    ShellUtils.execCommand("screencap -p " + screenshotFile + "/" + name
//                            +"-"+ result + new Date().getTime()
                            + ".png", true);
                }
            }).start();
        }

        else{
//            logcat_sp = context.getSharedPreferences("logcat_path", Activity.MODE_PRIVATE);
//            logcat_sp.getString("logcat_path", "");
//            ta_name = intent.getStringExtra("NAME");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    ShellUtils.execCommand("screencap -p " +   logcat_sp.getString("logcat_path", "") + "/" + ta_name
//                            + ".png", true);
//                }
//            }).start();

            ta_name = intent.getStringExtra("NAME");
                        new Thread(new Runnable() {
                @Override
                public void run() {
                    ShellUtils.execCommand("screencap -p " + Settings.getDefaultSharedPreferences(context.getApplicationContext()).getString(Settings.KEY_LOGCAT_PATH, "") + "/" + ta_name
                            + ".png", true);
                }
            }).start();
        }

        /**
        new Thread(new Runnable() {
        @Override
        public void run() {
        File png = new File(screenshotFile + "/" + name + ".png");
        try {
        OutputStream out = new FileOutputStream(png);
        Bitmap bitmap = ScreenUtil.rawScreenshot().bitmap(0.3f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
        bitmap.recycle();
        } catch (Exception ignore) {
        }
        }
        }).start();
       **/

    }
}
