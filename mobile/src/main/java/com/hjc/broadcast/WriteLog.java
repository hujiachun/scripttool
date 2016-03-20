package com.hjc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hujiachun on 2016/3/19.
 */
public class WriteLog extends BroadcastReceiver{
    String resultpath;

    @Override
    public void onReceive(Context context, Intent intent) {

        resultpath =  Settings.getDefaultSharedPreferences(context.getApplicationContext()).getString(Settings.KEY_LOGCAT_PATH, "");
        try {

            writeLog(resultpath, intent.getStringExtra("log"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeLog(String path, String line) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + "/logcat.txt" , true);
        fos.write(line.getBytes());
        fos.write("\n".getBytes());//换行
        fos.flush();
        fos.close();
    }
}
