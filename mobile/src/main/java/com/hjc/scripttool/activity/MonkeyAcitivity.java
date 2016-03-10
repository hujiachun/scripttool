package com.hjc.scripttool.activity;

import android.app.Activity;
import android.app.ILogactService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hjc.scripttool.R;
import com.hjc.scriptutil.Appinfo;
import com.hjc.service.TimerService;
import com.hjc.util.Constants;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by hujiachun on 15/11/5.
 */
public class MonkeyAcitivity extends Activity {
    EditText touch, motion, trackball, nav, majornav, syskeys, appswitch, anyevent, seed, throttle, runtime, packagename;
    TextView command;
    static int time;
    static String touch_text, motion_text, trackball_text, nav_text, majornav_text, syskeys_text,
            appswitch_text, anyevent_text, seed_text, throttle_text, runtime_text, command_text, package_text;
    static String touchvalue, motionwalue, trackballvalue, navvalue, majornavvalue,
            syskeysvalue, appswitchvalue, anyeventvalue, crashesvalue = "", timeoutsvalue = "", killvalue = "", sendvalue, throttlevalue, runtimevalue;
    CheckBox crashes, timeouts, killprocess;
    boolean isconfirm = false;
    File file;
    String path = null;
    public ArrayList<Appinfo> appinfos;
    static ProgressDialog progress;


    public static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            progress.dismiss();

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monkeyactivity);
        appinfos = new ArrayList<>();
        touch = (EditText) this.findViewById(R.id.touch);
        motion = (EditText) this.findViewById(R.id.motion);
        trackball = (EditText) this.findViewById(R.id.trackball);
        nav = (EditText) this.findViewById(R.id.nav);
        majornav = (EditText) this.findViewById(R.id.majornav);
        syskeys = (EditText) this.findViewById(R.id.syskeys);
        appswitch = (EditText) this.findViewById(R.id.appswitch);
        anyevent = (EditText) this.findViewById(R.id.anyevent);
        seed = (EditText) this.findViewById(R.id.seed);
        throttle = (EditText) this.findViewById(R.id.throttle);
        runtime = (EditText) this.findViewById(R.id.count);

        packagename = (EditText) this.findViewById(R.id.packagename);

        command = (TextView) this.findViewById(R.id.command);

        crashes = (CheckBox) this.findViewById(R.id.crashes);
        timeouts = (CheckBox) this.findViewById(R.id.timeouts);
        killprocess = (CheckBox) this.findViewById(R.id.kill);
        crashes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    crashesvalue = " --ignore-crashes ";
                }
            }
        });


        timeouts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    timeoutsvalue = " --ignore-timeouts ";
                }
            }
        });

        killprocess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    killvalue = " --kill-process-after-error ";
                }

            }
        });

        Button btn1 = (Button) this.findViewById(R.id.btn1);
        Button btn2 = (Button) this.findViewById(R.id.btn2);
        Button btn3 = (Button) this.findViewById(R.id.btn3);


        btn1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                touch_text = touch.getText().toString();
                motion_text = motion.getText().toString();
                trackball_text = trackball.getText().toString();
                nav_text = nav.getText().toString();
                majornav_text = majornav.getText().toString();
                syskeys_text = syskeys.getText().toString();
                appswitch_text = appswitch.getText().toString();
                anyevent_text = anyevent.getText().toString();
                seed_text = seed.getText().toString();
                throttle_text = throttle.getText().toString();
                runtime_text = runtime.getText().toString();


                if (!touch_text.equals("")) {
                    touchvalue = " --pct-touch " + touch_text;
                } else {
                    touchvalue = "";
                }
                if (!motion_text.equals("")) {
                    motionwalue = " --pct-motion " + motion_text;
                } else {
                    motionwalue = "";
                }
                if (!trackball_text.equals("")) {
                    trackballvalue = " --pct-trackball " + trackball_text;
                } else {
                    trackballvalue = "";
                }
                if (!nav_text.equals("")) {
                    navvalue = " --pct-nav " + nav_text;
                } else {
                    navvalue = "";
                }
                if (!trackball_text.equals("")) {
                    majornavvalue = " --pct-majornav " + majornav_text;
                } else {
                    majornavvalue = "";
                }
                if (!syskeys_text.equals("")) {
                    syskeysvalue = " --pct-syskeys " + syskeys_text;
                } else {
                    syskeysvalue = "";
                }
                if (!appswitch_text.equals("")) {
                    appswitchvalue = " --pct-appswitch " + appswitch_text;
                } else {
                    appswitchvalue = "";
                }
                if (!anyevent_text.equals("")) {
                    anyeventvalue = " --pct-anyevent " + anyevent_text;
                } else {
                    anyeventvalue = "";
                }
                if (!seed_text.equals("")) {
                    sendvalue = " -s " + seed_text;
                } else {
                    sendvalue = "";
                }
                if (!throttle_text.equals("")) {
                    throttlevalue = " --throttle " + throttle_text;
                } else {
                    throttlevalue = "";
                }
                if (!runtime_text.equals("")) {
                    runtimevalue = runtime_text;
                } else {
                    runtimevalue = "";
                }


                if (runtimevalue == "") {
                    Toast.makeText(getApplicationContext(), "please enter Monkey Test runtime", Toast.LENGTH_SHORT).show();
                } else if (package_text == null) {
                    Toast.makeText(getApplicationContext(), "please enter APP package", Toast.LENGTH_SHORT).show();
                } else {
                    Date date = new Date(new Date().getTime());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    String str = format.format(date);

                    file = new File(Constants.MONKEY_PATH + str + "-" + packagename.getText().toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    path = file.getAbsolutePath();


                    command_text = " monkey " + package_text + touchvalue + motionwalue + trackballvalue + navvalue + majornavvalue +
                            syskeysvalue + appswitchvalue + anyeventvalue + crashesvalue + timeoutsvalue
                            + " --monitor-native-crashes --ignore-security-exceptions --monitor-native-crashes "
                            + " -v -v -v " + throttlevalue + sendvalue + killvalue + " " + 999999999

//                            + " >>" + path + "/monkeylog.txt"
                            + " 1>>" + path + "/monkeylog.txt 2>&1 &"
                    ;
                    command.setText(command_text);
                    time = Integer.parseInt(runtimevalue);
                    isconfirm = true;
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isconfirm == false) {
                    Toast.makeText(getApplicationContext(), "please confirm", Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Util.stopLogCat(getApplicationContext());
                        }
                    }).start();

                    Util.startLogcat(getApplicationContext(), file);

                    //logcat路径
                    SharedPreferences logcat_sp = getSharedPreferences("logcat_path", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor logcat_editor = logcat_sp.edit();
                    logcat_editor.putString("logcat_path", file.getAbsolutePath());
                    logcat_editor.commit();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtils.execCommand(command_text, true);

                        }
                    }).start();

                    Intent intent = new Intent(MonkeyAcitivity.this, TimerService.class);
                    intent.putExtra(Constants.TIME, time);
                    startService(intent);
                    finish();
                    Util.pressHome(getApplicationContext());
                }

            }

        });

        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ResultListActivity.class);
                startActivity(intent);

            }
        });

        Button btn_package = (Button) this.findViewById(R.id.btn_package);
        btn_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(MonkeyAcitivity.this, "Loading...", "Please wait...", true, false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.TYPE, Constants.THREE);
                        intent.setClass(getApplicationContext(), PackageActivity.class);
                        startActivityForResult(intent, 100);
                        handler.sendMessage(new Message());
                    }
                }).start();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            packagename = (EditText) this.findViewById(R.id.packagename);
            packagename.setText(bundle.getString("appname"));
            package_text = bundle.getString("packagename");
            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    public void installPackageSystem(View v) throws IOException, InterruptedException {

        progress = ProgressDialog.show(MonkeyAcitivity.this, "Loading...", "Please wait...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(Constants.TYPE, Constants.SYSTEM);
                intent.setClass(MonkeyAcitivity.this, PackageActivity.class);
                startActivityForResult(intent, 100);
                handler.sendMessage(new Message());
            }
        }).start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isconfirm = false;
    }




    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

    }



}


