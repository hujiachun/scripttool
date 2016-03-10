package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hjc.broadcast.Wifi;
import com.hjc.scripttool.R;
import com.hjc.service.PerformanceService;
import com.hjc.util.Constants;
import com.hjc.util.Copier;
import com.hjc.util.Util;
import com.hjc.util.ZipCompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.view.hack.HackyDrawerLayout;


/**
 * Created by hujiachun on 15/11/5.
 */
public class MainActivity extends Activity{
    public String str;
//    ProgressDialog progress;
//    ArrayList<String> jarlist = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
//        SuSheller.clearTestCache(true);
        final SharedPreferences.Editor edit;
        final SharedPreferences sp = getSharedPreferences("wifi", Activity.MODE_PRIVATE);
        edit = sp.edit();

//        edit.putBoolean("switch", true);
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
//        Intent intent = new Intent();
//        intent.setClass(getApplicationContext(), PerformanceService.class);
//        startService(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("172.16.152.20", 8384), 10000);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader br = null;

                try {
//                    if(socket != null){
                        InputStream sis = socket.getInputStream();
                        br = new BufferedReader(new InputStreamReader(sis));
                        try {

                            String line = br.readLine();
                            Log.e(Constants.TAG, line);

                            Message ms = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("str", line);
                            ms.setData(bundle);
                            hd.sendMessage(ms);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                    }
//                    else {
//                        hd.sendMessage(new Message());
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();




    }


    public void performance(View v) {

//        ArrayList<String> data = Util.readCsv("/sdcard/Result/performance/2016-03-02-13-17-54/data.csv", getApplicationContext());
//
        Intent intent = new Intent();
//        intent.putStringArrayListExtra("data", data);
        File file = new File(Constants.PERFORMANCE_PATH);
        String[] list = file.list();

        List<String> hisList = new ArrayList<>(Arrays.asList(list));
        intent.putStringArrayListExtra(Constants.PERFORMANCE_LIST, (ArrayList<String>) hisList);
        intent.setClass(getApplicationContext(), PerformanceHistoryAcitity.class);
        startActivity(intent);
    }

//        jarlist = (ArrayList<String>) LeadJarServices.getSystemJarList();
//        Intent intent = new Intent();
//        intent.putStringArrayListExtra("jarlist", jarlist);
//        intent.setClass(getApplicationContext(), AlibabaActivity.class);
//        startActivity(intent);

//    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
//            progress.dismiss();// 关闭ProgressDialog
//        }
//    };
}
