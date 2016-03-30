package com.hjc.broadcast;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import com.hjc.util.WifiAutoConnectManager;


/**
 * Created by hujiachun on 15/11/26.
 */
public class Wifi extends BroadcastReceiver {
    WifiManager wifiManager;
    private SharedPreferences preferences;
    @Override
    public void onReceive(Context context, Intent intent) {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

        preferences = Settings.getDefaultSharedPreferences(context);
        String ssid = preferences.getString(Settings.KEY_WIFI_SSID, Constants.WIFI_SSID);
        String pwd = preferences.getString(Settings.KEY_WIFI_PWD, Constants.WIFI_PWD);
        boolean open = preferences.getBoolean(Settings.KEY_WIFI_STATE, true);
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if ((WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) && open)

            switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wifiManager.setWifiEnabled(true);
                    }
                }).start();

                break;

            case WifiManager.WIFI_STATE_ENABLED:

                break;
        }


        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction()) && open) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                NetworkInfo.State state = networkInfo.getState();
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态

                boolean isConnected = networkInfo.isAvailable();

                if (isConnected) {

                    if(!wifiManager.getConnectionInfo().getSSID().toString().equals("\"" + ssid + "\"")){
                        WifiAutoConnectManager wfat = new WifiAutoConnectManager(wifiManager);
                        wfat.connect(ssid, pwd, WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                    }

                } else {
                        WifiAutoConnectManager wfat = new WifiAutoConnectManager(wifiManager);
                        wfat.connect(ssid, pwd, WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                }

                }
            }
        }




}