package com.hjc.scripttool.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hjc.broadcast.Wifi;
import com.hjc.scripttool.R;
import com.hjc.scriptutil.Appinfo;
import com.hjc.scriptutil.Settings;
import com.hjc.service.PerformanceService;
import com.hjc.util.Constants;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;
import com.hjc.util.WifiTool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by hujiachun on 15/12/8.
 */
public class SettingActivity extends Activity{
    private EditText ssid, pwd;
    private TextView logpath, tvTime, packageName;
    private WifiManager wifi;
    private WifiTool wifiTool;
    private List<ScanResult> list;
    private int pid = 0;
    private ArrayList<String> ischecked_type;
    private Switch wifi_switch, logcat_switch, performance_switch, heap_switch;
    private SeekBar timeBar;
    private Dialog dialog;
    private SharedPreferences preferences;
    private RelativeLayout select_package;
    private ArrayList<Appinfo> appinfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        final SharedPreferences.Editor edit;
        wifi_switch = (Switch) findViewById(R.id.wiperSwitch1);
        logcat_switch = (Switch) findViewById(R.id.wiperSwitch2);
        performance_switch = (Switch) findViewById(R.id.wiperSwitch3);
        heap_switch = (Switch) findViewById(R.id.wiperSwitch4);
        logpath = (TextView) findViewById(R.id.logpath);
        tvTime = (TextView) findViewById(R.id.time);
        packageName = (TextView) findViewById(R.id.packagename_text);
        timeBar = (SeekBar) findViewById(R.id.timeline);
        select_package = (RelativeLayout) findViewById(R.id.select_package);

        appinfos = new ArrayList<>();
        preferences = Settings.getDefaultSharedPreferences(getApplicationContext());
        performance_switch.setChecked(preferences.getBoolean(Settings.KEY_PERFORMANCE, false));
        logcat_switch.setChecked(preferences.getBoolean(Settings.KEY_LOGCAT_STATE, false));
        heap_switch.setChecked(preferences.getBoolean(Settings.KEY_HEAP_STATE, false));
        wifi_switch.setChecked(preferences.getBoolean(Settings.KEY_WIFI_STATE, true));


        if (preferences.getBoolean(Settings.KEY_LOGCAT_STATE, false)){
           logpath.setText(preferences.getString(Settings.KEY_LOGCAT_PATH, ""));
       }

        ssid = (EditText) findViewById(R.id.ssid);
        pwd = (EditText) findViewById(R.id.pwd);

        ssid.setText(preferences.getString(Settings.KEY_WIFI_SSID, Constants.WIFI_SSID));
        pwd.setText(preferences.getString(Settings.KEY_WIFI_PWD, Constants.WIFI_PWD));

        wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {

                    preferences.edit().putString(Settings.KEY_WIFI_SSID, ssid.getText().toString()).
                            putString(Settings.KEY_WIFI_PWD, pwd.getText().toString()).
                            putBoolean(Settings.KEY_WIFI_STATE, true).commit();
                    Toast.makeText(getApplicationContext(), "bind", Toast.LENGTH_SHORT).show();

                } else {

                    preferences.edit().putString(Settings.KEY_WIFI_SSID, ssid.getText().toString()).
                            putString(Settings.KEY_WIFI_PWD, pwd.getText().toString()).
                            putBoolean(Settings.KEY_WIFI_STATE, false).commit();
                    Toast.makeText(getApplicationContext(), "unbind", Toast.LENGTH_SHORT).show();
                }
            }
        });


        logcat_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Date date = new Date(new Date().getTime());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    String str = format.format(date);
                    final File file = new File(Environment.getExternalStorageDirectory() + "/Result/LogCat/" + str + "/");
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    preferences.edit().putString(Settings.KEY_LOGCAT_PATH, file.getAbsolutePath()).putBoolean(Settings.KEY_LOGCAT_STATE, true).commit();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Util.startLogcat(getApplicationContext(), file);

                        }
                    }).start();

                    logpath.setText("/sdcard/Result/LogCat/" + str);
                    Toast.makeText(getApplicationContext(), "logcat is running ", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Util.stopLogCat(getApplicationContext());
                        }
                    }).start();
                    preferences.edit().putBoolean(Settings.KEY_LOGCAT_STATE, false).commit();
                    Toast.makeText(getApplicationContext(), "logcat killed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        timeBar.setProgress(Settings.getDefaultSharedPreferences(getApplicationContext()).getInt(Settings.KEY_INTERVAL, 5) - 1);
        tvTime.setText(Settings.getDefaultSharedPreferences(getApplicationContext()).getInt(Settings.KEY_INTERVAL, 5) + "s");

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                tvTime.setText(Integer.toString(arg1 + 1) + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // when tracking stoped, update preferences
                int interval = arg0.getProgress() + 1;
                preferences.edit().putInt(Settings.KEY_INTERVAL, interval).commit();
            }
        });

        heap_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){

                        if(Util.upgradeRootPermission(getPackageCodePath())){
                             preferences.edit().putBoolean(Settings.KEY_HEAP_STATE, true).commit();
                            heap_switch.setChecked(true);
                         }
                         else{
                            heap_switch.setChecked(false);
                            Toast.makeText(getApplicationContext(), "authorization failed", Toast.LENGTH_SHORT).show();
                        }
                }
                else {
                    preferences.edit().putBoolean(Settings.KEY_HEAP_STATE, false).commit();
                }
            }
        });

        performance_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    if(packageName.getText().equals(Constants.DEFAULT_PACKAGE)){
                        Toast.makeText(getApplicationContext(), "Select Package", Toast.LENGTH_SHORT).show();
                        performance_switch.setChecked(false);
                    }
                    else{
                        preferences.edit().putBoolean(Settings.KEY_PERFORMANCE, true).commit();
                    }
                }
                else {
                    stopService(new Intent().setClass(getApplicationContext(), PerformanceService.class));
                    preferences.edit().putBoolean(Settings.KEY_PERFORMANCE, false).commit();
                }

            }
        });

        select_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra(Constants.TYPE, Constants.THREE).putExtra(Constants.PERFORMANCE, true);
                intent.setClass(getApplicationContext(), PackageActivity.class);
                startActivityForResult(intent, 100);
            }
        });


    }


    @Override
    protected void onStart() {
        packageName.setText(preferences.getString(Settings.KEY_PACKAGE, Constants.DEFAULT_PACKAGE));
        super.onStart();
    }

    public void selectWifi(View v) {
        wifiTool = new WifiTool(getApplicationContext());
        wifiTool.openWifi();
        list = wifiTool.getScanResults();
        Set<String> ssidList = wifiTool.deleteSame(list);
        final String[] types = ssidList.toArray(new String[0]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
         dialog = builder.setSingleChoiceItems(types, types.length, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                        ssid.setText(types[i]);
                        if(types[i].equals(Constants.WIFI_SSID)){
                            pwd.setText(Constants.WIFI_PWD);
                            wifi_switch.setChecked(true);
                        }
                        else{
                            pwd.setText("");
                            wifi_switch.setChecked(false);
                        }

                        dialog.dismiss();
            }
        }).show();
    }


    public void clear(View v){

        ischecked_type = new ArrayList<>();
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("clear history")
                .setNegativeButton("cancel", null).setPositiveButton("clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(ischecked_type.size() != 0){
                    for (String type : ischecked_type) {
                        File file = new File("/sdcard/Result/" + type);
                        deleteDir(file);
                    }
                    Toast.makeText(getApplicationContext(), "clear success", Toast.LENGTH_SHORT).show();
                }
                else {
                    builder.show();
                    Toast.makeText(getApplicationContext(), "please select type", Toast.LENGTH_SHORT).show();
                }



            }
        });

        final String[] types = new String[]{"logcat", "monkey", "uiautomator", "performance"};
        builder.setMultiChoiceItems(types, new boolean[]{false, false, false, false}, new DialogInterface.OnMultiChoiceClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if(b){
                    ischecked_type.add(types[i]);
                }
                else{
                    ischecked_type.remove(types[i]);
                }

            }
        }).show();

    }


    public void openFile(View v){
        if(logpath.getText().equals("open logcat")){

        }
        else{
            File file = new File(getSharedPreferences("logcat_path", Activity.MODE_PRIVATE).getString("logcat_path", ""));
            new AlertDialog.Builder(SettingActivity.this).setItems(file.list(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }


    }


    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
