package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.hjc.broadcast.Wifi;
import com.hjc.http.FormFile;
import com.hjc.http.Http;
import com.hjc.http.NewsService;
import com.hjc.http.SocketHttpRequester;
import com.hjc.scripttool.R;
import com.hjc.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hujiachun on 15/11/5.
 */
public class MainActivity extends Activity{
    public String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        final SharedPreferences.Editor edit;
        final SharedPreferences sp = getSharedPreferences("wifi", Activity.MODE_PRIVATE);
        edit = sp.edit();
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(sp.getString("ssid", "") == ""){
            edit.putBoolean("state", true);
            wifi.setWifiEnabled(false);
            edit.putString("ssid", "MZtest");
            edit.putString("pwd", "Meizu@test123");
            edit.commit();
            sendBroadcast(new Intent(getApplicationContext(), Wifi.class));
        }
        else {
            sendBroadcast(new Intent(getApplicationContext(), Wifi.class));
        }


//        File uitest = new File(getFilesDir().getPath() + "/uitest.zip");
//        if(!uitest.exists()){
//            try {
//                Copier.copyFile(getResources().openRawResource(R.raw.uitest), new FileOutputStream(uitest));
//                ZipCompressor.decompress(uitest);
//               com.hjc.util.Writer.chmodEverythingBySystem(getFilesDir().getPath() + "/uitest/");
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void gotoUiautomator(View v){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), UiautomatorActivity.class);
        startActivity(intent);

    }

    public void gotoMonkey(View v){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MonkeyAcitivity.class);
        startActivity(intent);

    }


    public void gotosetting(View v){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SettingActivity.class);
        startActivity(intent);

    }


    public Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        str = msg.getData().getString("str");
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };




    public void test1(View v) throws IOException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                ZipCompressor.zip(new File("/storage/emulated/0/Result/performance/"));
////                Http.upload("/storage/emulated/0/Result/testResult.zip", "http://172.16.11.126:8000/account/uploaddone/");
//                int state = Http.upload("/storage/emulated/0/Result/gps_stats.txt", "http://172.16.11.126:8000/account/upfile/");
//                Log.e(Constants.TAG, " " + state);
//            }
//        }).start();
        startActivity(new Intent().setClass(getApplicationContext(), TestActivity.class));

    }


    public void performance(View v) {
        Intent intent = new Intent();
        File file = new File(Constants.PERFORMANCE_PATH);
        String[] list = file.list();
        if(list != null){
            List<String> hisList = new ArrayList<>(Arrays.asList(list));
            intent.putStringArrayListExtra(Constants.PERFORMANCE_LIST, (ArrayList<String>) hisList);
            intent.setClass(getApplicationContext(), PerformanceHistoryAcitity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
        }
    }

}
