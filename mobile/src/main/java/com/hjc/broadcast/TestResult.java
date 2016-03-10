package com.hjc.broadcast;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.hjc.scriptutil.Settings;
import com.hjc.util.FileUtil;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hujiachun on 15/11/17.
 */
public class TestResult extends BroadcastReceiver {
    public Context context;
    String name;
    boolean result;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        name = intent.getStringExtra("NAME");
        result = intent.getExtras().getBoolean("RESULT");

//        SharedPreferences sp = context.getSharedPreferences("path", Activity.MODE_PRIVATE);
//        final String resultpath = sp.getString("resultpath", "");
        final String resultpath = Settings.getDefaultSharedPreferences(context).getString(Settings.KEY_UIAUTOMATOR, "");

        final File screenshotFile = new File(resultpath + "/screenshot");
        if(!screenshotFile.exists()){
            screenshotFile.mkdirs();
        }



        File fi = new File(resultpath + "/result.txt");
        FileUtil.removeLineFromFile(new File(resultpath + "/result.txt").getAbsolutePath(), name);
        try {
            FileOutputStream fos = new FileOutputStream(fi, true);
            fos.write(name.getBytes());
            fos.write(":".getBytes());
            fos.write((result ? "true" : "false").getBytes());
            fos.write("\n".getBytes());//换行

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent().setAction("st.take").putExtra("name", name).putExtra("result", result).putExtra("screenshotFile", screenshotFile.getAbsolutePath()));

        Toast.makeText(context, name + ":" + result, Toast.LENGTH_SHORT).show();


    }



}
