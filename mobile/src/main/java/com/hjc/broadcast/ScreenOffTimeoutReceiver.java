package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;



public class ScreenOffTimeoutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int timeout = intent.getIntExtra("TIME_OUT", 0);
        if (timeout > 0) {
            try {

                int screenOffTime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
                if (screenOffTime != timeout) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
