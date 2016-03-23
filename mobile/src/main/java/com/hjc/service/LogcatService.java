package com.hjc.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ILogactService;

import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

/**
 * Created by hujiachun on 15/12/15.
 */
public class LogcatService extends Service{
    private IBinder binder = new LogCatBinder();
    String resultpath ,logString = null;
    public Intent intent;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            logString =(String) msg.obj;
            if(logString != null){
                sendBroadcast(new Intent().putExtra("log", logString).setAction("sk.action.WRITELOG"));
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         this.intent = intent;
        return super.onStartCommand(intent, flags, startId);
    }

    public void logcat() throws IOException {
        resultpath =  Settings.getDefaultSharedPreferences(getApplicationContext()).getString(Settings.KEY_LOGCAT_PATH, "");

                String[] commands = {"logcat -c ", "logcat -v time *:E "
                        + " >>" + resultpath + "/logcat.txt", };
                ShellUtils.execCommand(commands, true);

//        Process p = null;
//        BufferedReader buffer = null;
//        try {
//            p = Runtime.getRuntime().exec("logcat -v time ");
//            buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line = null;
//            while ((line = buffer.readLine()) != null) {
//                handler.sendMessage(handler.obtainMessage(1, 1, 1, line));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(p != null){
//            p.destroy();
//        }
//        try {
//            buffer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private final class LogCatBinder extends ILogactService.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void startlogcat() throws RemoteException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logcat();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
