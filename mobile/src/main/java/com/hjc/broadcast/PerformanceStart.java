package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.hjc.scriptutil.Settings;
import com.hjc.service.PerformanceService;
import com.hjc.util.Constants;

/**
 * Created by hujiachun on 16/3/7.
 */
public class PerformanceStart extends BroadcastReceiver{
    private SharedPreferences preferences;
    private boolean performanceService;
    public String name;

    @Override
    public void onReceive(Context context, Intent intent) {

        preferences = Settings.getDefaultSharedPreferences(context);
        performanceService = preferences.getBoolean(Settings.KEY_PERFORMANCE, false);
        if(performanceService){
            name = intent.getStringExtra(Constants.CASE_NAME);
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            preferences.edit().putString(Settings.KEY_CASE, name).commit();

            intent.setClass(context, PerformanceService.class);
            context.startService(intent);
        }

    }
}
