package com.hjc.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.IOException;
import android.app.ILogactService;

import com.hjc.scriptutil.Settings;
import com.hjc.util.ShellUtils;

/**
 * Created by hujiachun on 15/12/15.
 */
public class LogcatService extends Service{
    private IBinder binder = new LogCatBinder();
    String resultpath;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    public void logcat() throws IOException {
//        SharedPreferences sp = getSharedPreferences("logcatpath", Activity.MODE_PRIVATE);
//        resultpath = sp.getString("resultpath", "");
        resultpath =  Settings.getDefaultSharedPreferences(getApplicationContext()).getString(Settings.KEY_LOGCAT_PATH, "");

                String[] commands = {"logcat -c ", "logcat -v time *:E "
                        + " >>" + resultpath + "/exec.txt", };
                ShellUtils.execCommand(commands, true);

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
