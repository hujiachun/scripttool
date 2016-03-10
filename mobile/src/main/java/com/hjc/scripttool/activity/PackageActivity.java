package com.hjc.scripttool.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.hjc.scripttool.view.AppinfoView;
import com.hjc.scripttool.R;
import com.hjc.scriptutil.Appinfo;
import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Created by hujiachun on 15/11/7.
 */
public class PackageActivity extends Activity{

    private AppinfoView adapter;
    private ListView listView;
    private ArrayList<Appinfo> appinfos;
    private String package_str = "", appname = "";
    private boolean isPerformance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packagelist);

        appinfos = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));// 排序

        Intent intent= getIntent();
        String type = intent.getStringExtra(Constants.TYPE);
        isPerformance = intent.getBooleanExtra(Constants.PERFORMANCE, false);
        if(type.equals(Constants.SYSTEM)){
            for (ApplicationInfo app : listAppcations) {//系统
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    Appinfo appinfo = new Appinfo();
                    appinfo.setPackageName(app.packageName);
                    appinfo.setName(app.loadLabel(pm).toString());
                    appinfo.setIcon(app.loadIcon(pm));
                    appinfos.add(appinfo);
                }
            }
        }
        else
        {
            for (ApplicationInfo app : listAppcations) {//第三方应用
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    Appinfo appinfo = new Appinfo();
                    appinfo.setPackageName(app.packageName);
                    appinfo.setName(app.loadLabel(pm).toString());
                    appinfo.setIcon(app.loadIcon(pm));
                    appinfos.add(appinfo);
                }
            }
        }

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new AppinfoView(this, appinfos, R.layout.packageitem);
        listView.setAdapter(adapter);

        Button btn_ok = (Button) this.findViewById(R.id.ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPerformance){//performance

                    for (int i = 0; i < adapter.getSelected_appname().size(); i++) {
                        if (adapter.getSelected_appname().size() == 1) {
                            package_str = adapter.getSelected_packagename().get(i);
                            appname = adapter.getSelected_appname().get(i);
                            Settings.getDefaultSharedPreferences(getApplicationContext()).edit().
                                    putString(Settings.KEY_PACKAGE, appname + ":" + package_str).commit();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Can only choose one", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {//monekey

                  for (int i = 0; i < adapter.getSelected_appname().size(); i++) {
                    if (i == 0) {
                        package_str = "-p " + adapter.getSelected_packagename().get(i);
                        appname = adapter.getSelected_appname().get(i);
                    } else {
                        package_str = package_str + " -p " + adapter.getSelected_packagename().get(i);
                        appname = appname + "," + adapter.getSelected_appname().get(i);
                    }
                }

                if (package_str == "") {
                    Toast.makeText(getApplicationContext(), "please select packages", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MonkeyAcitivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("packagename", package_str);
                    bundle.putString("appname", appname);
                    intent.putExtras(bundle);
                    setResult(0, intent);
                    finish();
                 }
              }
            }
        });
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    public void closeActivity(View v){
        this.finish();
    }

}
