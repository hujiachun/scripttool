package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hjc.scriptutil.Settings;
import com.hjc.service.PerformanceService;

/**
 * Created by hujiachun on 16/3/7.
 */
public class PerformanceStop extends BroadcastReceiver{
    private SharedPreferences preferences;
    private boolean performanceService;
    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = Settings.getDefaultSharedPreferences(context);
        performanceService = preferences.getBoolean(Settings.KEY_PERFORMANCE, false);
        if(performanceService){
            intent.setClass(context, PerformanceService.class);
            context.stopService(intent);
        }
    }
}
