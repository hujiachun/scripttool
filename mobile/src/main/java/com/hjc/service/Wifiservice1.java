package com.hjc.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hjc.util.WifiAutoConnectManager;

/**
 * Created by hujiachun on 15/12/7.
 */
public class Wifiservice1 extends IntentService {
    WifiManager wifiManager;

    public Wifiservice1() {
        super("wifi");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        Log.e("Test","onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("Test", "onStart");

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Test", "onHandleIntent");
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                Log.e("Test", "WIFI_STATE_DISABLED");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wifiManager.setWifiEnabled(true);
                    }
                }).start();
                break;

        }


        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态

                if (isConnected) {
                } else {

                    WifiAutoConnectManager wfat = new WifiAutoConnectManager(wifiManager);
                    wfat.connect("没钱就上网你想耍流氓", "123456789" , WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                }
            }
        }
    }


}

