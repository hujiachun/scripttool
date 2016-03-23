package com.hjc.service;

import android.app.ILogactService;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hjc.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hujiachun on 2016/3/19.
 */
public class LogCat extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void writeLog(String line) throws IOException {
        File file = new File("/sdcard" + "/result.txt");
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(line.getBytes());
        fos.write("\n".getBytes());//换行
        fos.flush();
    }

    public void logcatStart(){
        Process p = null;
        BufferedReader buffer = null;

        try {
            Runtime.getRuntime().exec("logcat - c").waitFor();
            p = Runtime.getRuntime().exec("logcat "
                    + "-v time *:E "
            );

            buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = buffer.readLine()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(p != null){
            p.destroy();
        }
        try {
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        logcatStart();
        return super.onStartCommand(intent, flags, startId);
    }



}
