package com.hjc.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by hujiachun on 16/1/25.
 */
public class TimerService extends IntentService {
    private int time;

    public TimerService() {
        super("TimerService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        time = intent.getIntExtra("time", 0);
        Log.e("scripttool", "service_time = " + time);

        try {
            Thread.sleep(time * 1000);
            monkeyHandler.sendMessage(new Message());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Handler monkeyHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("scripttool", "monkey over");
            sendBroadcast(new Intent().setAction("st.STOP.MONKEY"));
            super.handleMessage(msg);
        }
    };
}
