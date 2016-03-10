package com.hjc.scripttool.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjc.db.Auto;
import com.hjc.db.DBAutoservice;
import com.hjc.scripttool.view.MethodView;
import com.hjc.scripttool.R;
import com.hjc.util.Constants;
import com.hjc.util.ShellUtils;
import com.hjc.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by hujiachun on 15/11/18.
 */
public class MethodAcitvity extends Activity {
    public static MethodView adapter;
    ArrayList<String> methods, error_methods, traces_str;
    TextView casepass, casefail, resulttype, anr_number, fatal_number, crash_number;
    static boolean isClickError = false;
    public String type, time;
    ArrayList<String> crash, anr, fatal;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Util.stopLogCat(getApplicationContext());
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.methodlist);
        LinearLayout resutlist = (LinearLayout) this.findViewById(R.id.resutlist);
        error_methods = new ArrayList<>();

        Intent intent= getIntent();//得到UiautomatorActivity传值信息
        if(intent.getIntExtra("request", 0) == 1){
            resutlist.setVisibility(View.VISIBLE);
        }
        methods = intent.getStringArrayListExtra(Constants.METHODS);
        error_methods = intent.getStringArrayListExtra(Constants.ERROR_METHODS);
        type = intent.getStringExtra("type");
        time = intent.getStringExtra("time");

        ListView methodlist = (ListView) this.findViewById(R.id.methodlist);
        adapter = new MethodView(this, methods, R.layout.methoditem,
                new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + time + "/" + type + "/screenshot"), error_methods);


        methodlist.setAdapter(adapter);
//        methodlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Button btn_ok = (Button) this.findViewById(R.id.ok);
        Button btn_error = (Button) this.findViewById(R.id.error);

        casepass = (TextView) this.findViewById(R.id.casepass);
        casefail = (TextView) this.findViewById(R.id.casefail);
        crash_number = (TextView) this.findViewById(R.id.crash_number);
        anr_number = (TextView) this.findViewById(R.id.anr_number);
        fatal_number = (TextView) this.findViewById(R.id.fatal_number);


        File file = new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + time + "/" + type + "/exec.txt");
        if (file.exists()) {
            crash = new ArrayList<>();
            anr = new ArrayList<>();
            fatal = new ArrayList<>();
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (lineTxt.contains("ANR in com.")) {
                        anr.add(lineTxt);
                        int lenght =new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + time + "/" + type).list().length;

                        if(lenght == 4){

                            traces_str = new ArrayList<>();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    File file = new File("/data/anr/");
                                    for(String list : file.list()){
                                        if(list.contains("traces")){
                                            traces_str.add(list);
                                        }
                                    }

                                    for(int i = 0; i < traces_str.size(); i++){
                                        ShellUtils.execCommand("cat /data/anr/" + traces_str.get(i) + " > " + Environment.getExternalStorageDirectory() + "/Result/Uiautomator/"
                                                + time + "/" + type + "/traces_" + i + ".txt", true);
                                    }
                                }
                            }).start();
                        }
                    }
                    if (lineTxt.contains("CRASH:")) {
                        crash.add(lineTxt);
                    }
                    if (lineTxt.contains("FATAL EXCEPTION:")) {
                        fatal.add(lineTxt);
                    }
                }

                bufferedReader.close();
                read.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(crash != null){
                crash_number.setText(crash.size() + "");
                crash_number.setTextColor(0Xffff0000);
            }
            if(anr != null){
                anr_number.setText(anr.size() + "");
                anr_number.setTextColor(0Xffff0000);
            }
            if(fatal != null){
                fatal_number.setText(fatal.size() + "");
                fatal_number.setTextColor(0Xffff0000);
            }
            if(anr.size() != 0 || crash.size() != 0 || fatal.size() != 0){
                Toast.makeText(getApplicationContext(), "Found Exception", Toast.LENGTH_SHORT).show();
            }


        }

        if(error_methods != null){
//                resulttype.setText(type + "  -->");
                casefail.setText(" " + error_methods.size());
                casepass.setText(" " + (methods.size() - error_methods.size()));
                casefail.setTextColor(0Xffff0000);

        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(adapter.isSelected_methodname.size() == 0){
                    Toast.makeText(getApplicationContext(), "please select case", Toast.LENGTH_SHORT).show();
                }
                else{

                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), UiautomatorActivity.class);
                    intent.putStringArrayListExtra(Constants.METHODS, adapter.getIsSelected_methodname());
                    if(type != null){
                        intent.putExtra("jar", type + ".jar");
                        DBAutoservice db = new DBAutoservice(getApplicationContext());
                        ArrayList<Auto> autos = db.selcet(type + ".jar");
                        intent.putExtra("class", autos.get(0).getTestcase());
                    }
                    setResult(0, intent);
                    finish();
                }
            }
        });

        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(error_methods == null){
                    Toast.makeText(getApplicationContext(), "not found history", Toast.LENGTH_SHORT).show();
                }

                else {
                    adapter.notifyDataSetChanged();
                    if (error_methods.size() == 0){
                        Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
                    }

                    else if (isClickError == true){
                        for(int i = 0; i < adapter.getCount(); i++){
                                adapter.getIsSelected().put(i, false);
                                adapter.isSelected_methodname.remove(adapter.getItem(i).toString());
                            }
                        adapter.notifyDataSetChanged();
                        isClickError = false;

                    }

                    else {
                        for(int i = 0; i < adapter.getCount(); i++){
                            if(error_methods.contains(adapter.getItem(i))){
                                adapter.getIsSelected().put(i, true);
                                adapter.isSelected_methodname.add(adapter.getItem(i).toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        isClickError = true;
                    }
                }

            }
        });
    }


    public void runError(View v){
        if(isClickError == false || adapter.isSelected_methodname.size() == 0){
            Toast.makeText(getApplicationContext(), "select error", Toast.LENGTH_SHORT).show();
        }
        else {
            DBAutoservice db = new DBAutoservice(getApplicationContext());
            ArrayList<Auto> autos = db.selcet(type + ".jar");


            String command = "uiautomator runtest /sdcard/" + autos.get(0).getType();
            for (int i = 0; i < adapter.isSelected_methodname.size(); i++) {
                command = command + " -c " + autos.get(0).getTestcase() + "#" + adapter.isSelected_methodname.get(i);
            }

            final String finalCommand = command;
            new Thread(new Runnable() {

                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + time + "/" + type);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
//                    SharedPreferences sp = getSharedPreferences("path", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.clear();
//                    editor.putString("resultpath", file.getAbsolutePath());
//                    editor.commit();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Util.stopLogCat(getApplicationContext());
                        }
                    }).start();

                    Util.startLogcat(getApplicationContext(), new File("/sdcard/Result/Uiautomator/" + time + "/" + type));

                    //logcat路径
//                    SharedPreferences logcat_sp = getSharedPreferences("logcat_path", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor logcat_editor = logcat_sp.edit();
//                    logcat_editor.putString("logcat_path", file.getAbsolutePath());
//                    logcat_editor.commit();


                    try {
                        ShellUtils.execCcommand(finalCommand + ">>" + "/sdcard/Result/Uiautomator/" + time + "/" + type + "/log.txt");
                        handler.sendMessage(new Message());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            finish();
            Util.pressHome(getApplicationContext());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClickError = false;
        error_methods = null;
        time = null;
    }
}
