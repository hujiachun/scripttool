package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hjc.scripttool.activity.UiautomatorActivity;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

import java.io.IOException;

/**
 * Created by hujiachun on 15/12/12.
 */
public class KillUiautomator extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    int pid = Util.getPid("uiautomator");
                    if (pid != 0) {

                        ShellUtils.execCommand("kill " + pid, true);
                        Looper.prepare();
                        Toast.makeText(context, "killed", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, "not running", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
