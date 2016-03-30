package com.hjc.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by hujiachun on 16/3/30.
 */
public class WifiTool {
    public WifiManager wifiManager;

    public WifiTool(Context context) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void openWifi()  {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi()  {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public List<ScanResult> getScanResults(){
        wifiManager.startScan();
        return wifiManager.getScanResults();
    }


    /**
     * 得到SSID
     * @param list
     */
    public Set<String> deleteSame(List<ScanResult> list){
        Set<String> ssidList = new HashSet();//去除重复ssid
        for(int i = 0; i < list.size(); i++){
            String wifi_ssid = list.get(i).SSID;
            if(wifi_ssid != null){
                ssidList.add(wifi_ssid);
            }
        }
        return ssidList;
    }




}
