package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by hujiachun on 15/12/17.
 */
public class KillMonkey extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int pid = getPid("com.hjc.scripttool");
                            if (pid != 0) {

                                Runtime.getRuntime().exec("kill " + pid);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        }

    public int getPid(String tag) throws IOException {
        Process p;
        p = Runtime.getRuntime().exec("ps ");
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = bufferedReader2.readLine()) != null) {
            if (line.contains(tag)) {
                return Integer.parseInt(line.split("\\s+")[1]);
            }
        }
        return -1;
    }
}
