package com.hjc.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.hjc.util.TimeUtil;
import java.util.Calendar;


public class SetTime extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        int defaultM = Calendar.getInstance().get(Calendar.MONTH);
        int year = intent.getIntExtra("YEAR", Calendar.getInstance().get(Calendar.YEAR));
        int month = intent.getIntExtra("MONTH", defaultM);
        int day = intent.getIntExtra("DAY", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        int hour = intent.getIntExtra("HOUR", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        int minute = intent.getIntExtra("MINUTE", Calendar.getInstance().get(Calendar.MINUTE));
        int second = intent.getIntExtra("SECOND", Calendar.getInstance().get(Calendar.SECOND));
        long time = intent.getLongExtra("TIME", 0);
        boolean auto = intent.getBooleanExtra("AUTO", true);

        if (auto) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    TimeUtil.restoreSystemTime(context);
                }
            });
            thread.start();
        } else {
            if(time != 0){
                TimeUtil.setSystemTime(context, time);
            }
            else {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, second);
                TimeUtil.setSystemTime(context, c.getTime());
            }
        }
    }
}
