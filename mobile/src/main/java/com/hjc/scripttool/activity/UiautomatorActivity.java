package com.hjc.scripttool.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ILogactService;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hjc.db.Auto;
import com.hjc.db.DBAutoservice;
import com.hjc.scripttool.R;
import com.hjc.scriptutil.LeadJarServices;
import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import com.hjc.util.Util;
import com.hjc.util.ShellUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class UiautomatorActivity extends Activity {
    public Button btn_jar, btn_class, btn_method, btn_run, btn_stop, btn_history;
    public EditText jar_text, class_text, method_text;
    public static String jar, classname, methodname, name;
    public static ArrayList<String> methodList;
    public static boolean class_isChicked = false;
    public static boolean method_isChicked = false;
    public static File file = null;
    public static Thread run_ui;
    private SharedPreferences preferences;
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
        setContentView(R.layout.uiautomatoractivity);
        btn_jar = (Button) this.findViewById(R.id.button1);
        btn_class = (Button) this.findViewById(R.id.button2);
        btn_method = (Button) this.findViewById(R.id.button3);
        btn_run = (Button) this.findViewById(R.id.button4);
        btn_stop = (Button) this.findViewById(R.id.button5);
        btn_history = (Button) this.findViewById(R.id.button6);
        jar_text = (EditText) this.findViewById(R.id.jar_text);
        class_text = (EditText) this.findViewById(R.id.class_text);
        method_text = (EditText) this.findViewById(R.id.method_text);
        methodList = new ArrayList<>();
        preferences = Settings.getDefaultSharedPreferences(getApplicationContext());

        btn_jar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> jares = LeadJarServices.getJarList();
                final String[] jar_string = jares.toArray(new String[0]);
                AlertDialog.Builder builder = new AlertDialog.Builder(UiautomatorActivity.this);

                builder.setTitle("Select Jar");
                builder.setItems(jar_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jar_text.setText(jar_string[which]);
                        jar = jar_string[which];//得到执行jar
                    }
                });
                builder.show();

            }
        });


        btn_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jar == null) {
                    Toast.makeText(UiautomatorActivity.this, "plaese enter jar", Toast.LENGTH_SHORT).show();
                } else {

                    List<String> classes = null;
                    try {
                        classes = LeadJarServices.getAllClasses(getApplicationContext(), new File("/sdcard/" + jar));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String[] class_string = classes.toArray(new String[0]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(UiautomatorActivity.this);
                    builder.setTitle("Select Class");
                    builder.setItems(class_string, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            class_text.setText(class_string[which]);
                            method_text.setText("ALL TESTCASE");
                            classname = class_string[which];//得到执行类
                            class_isChicked = true;
                            method_isChicked = true;
                        }
                    });
                    builder.show();
                }
            }
        });


        btn_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                methodList.clear();
                if (class_isChicked == false) {
                    Toast.makeText(UiautomatorActivity.this, "please enter method", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> methods = null;
                    try {
                        methods = (ArrayList) LeadJarServices.addTestClassesFromJars(getApplicationContext(), "/sdcard/" + jar, classname);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Constants.REQUEST, 0);
                    intent.putStringArrayListExtra(Constants.METHODS, methods);
                    intent.setClass(getApplicationContext(), MethodAcitvity.class);
                    startActivityForResult(intent, 100);
                    method_isChicked = true;
                }

            }
        });


        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (method_isChicked == false) {
                    Toast.makeText(UiautomatorActivity.this, "please enter method", Toast.LENGTH_SHORT).show();

                } else {
                    Date date = new Date(new Date().getTime());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    String str = format.format(date);


//                    file = new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + new Date().getTime() + "/" + jar.split(".jar")[0]);
                    file = new File(Environment.getExternalStorageDirectory() + "/Result/Uiautomator/" + str + "/" + jar.split(".jar")[0]);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Util.stopLogCat(getApplicationContext());
                        }
                    }).start();
                    Util.startLogcat(getApplicationContext(), file);


                    //logcat路径
//                    SharedPreferences logcat_sp = getSharedPreferences("logcat_path", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor logcat_editor = logcat_sp.edit();
//                    logcat_editor.putString("logcat_path", file.getAbsolutePath());
//                    logcat_editor.commit();
                    preferences.edit().putString(Settings.KEY_LOGCAT_PATH, file.getAbsolutePath()).
                            putBoolean(Settings.KEY_LOGCAT_STATE, true).
                            putString(Settings.KEY_TIME, str).commit();

                    Auto auto = new Auto();
                    auto.setType(jar);
                    auto.setTestcase(classname);
                    DBAutoservice db = new DBAutoservice(getApplicationContext());
                    db.delete(jar);
                    db.insert(auto);

//                    SharedPreferences sp = getSharedPreferences("path", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.clear();
//                    editor.putString("resultpath", file.getAbsolutePath());
//                    editor.commit();

                    preferences.edit().putString(Settings.KEY_LOGCAT_PATH, file.getAbsolutePath()).putString(Settings.KEY_UIAUTOMATOR, file.getAbsolutePath()).commit();
                    run_ui = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (method_text.getText().toString().equals("ALL TESTCASE")) {
                                try {
                                    ShellUtils.execCcommand("uiautomator runtest /sdcard/" + jar + " -c " + classname + ">" +
                                            file.getAbsolutePath() + "/log.txt");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String command = "uiautomator runtest /sdcard/" + jar;
                                for (int i = 0; i < methodList.size(); i++) {
                                    command = command + " -c " + classname + "#" + methodList.get(i);
                                }
                                try {
                                    ShellUtils.execCcommand(command + ">" + file.getAbsolutePath() + "/log.txt");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendMessage(new Message());
                        }
                    });
                    run_ui.start();
                    Util.pressHome(getApplicationContext());
                }
            }
        });


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendBroadcast(new Intent().setAction("st.STOP.UIAUTOMATOR"));
            }
});



        btn_history.setOnClickListener(new View.OnClickListener() {
            File file1 = new File("/sdcard/Result/Uiautomator");
            String[] file1list = file1.list();
            String time;
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UiautomatorActivity.this);
                builder1.setTitle("Select time")
                .setItems(file1list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        time = file1list[i];
                        File file2 = new File("/sdcard/Result/Uiautomator/" + time);
                        String type = file2.list()[0];

                        ArrayList<String> results = Util.readText(Settings.getDefaultSharedPreferences(getApplicationContext()).getString(Settings.KEY_UIAUTOMATOR, "")
                                + "/result.txt", getApplicationContext());
//                        ArrayList<String> results = Util.readText("/sdcard/Result/Uiautomator/" + time + "/" + type + "/result.txt", getApplicationContext());
                                ArrayList<String> methods = new ArrayList<>();
                                ArrayList<String> error_methods = new ArrayList<>();
                                for (int j = 0; j < results.size(); j++) {
                                    methods.add(results.get(j).split(":")[0]);
                                    if (results.get(j).contains("false")) {
                                        error_methods.add(results.get(j).split(":")[0]);
                                    }
                                }


                        Intent intent = new Intent();
                                intent.putExtra("time", time);
                                intent.putExtra("type", type);
                                intent.putExtra("request", 1);
                                Collections.sort(methods);
                                intent.putStringArrayListExtra(Constants.METHODS, methods);
                                intent.putStringArrayListExtra(Constants.ERROR_METHODS, error_methods);
                                intent.setClass(getApplicationContext(), MethodAcitvity.class);
                                startActivityForResult(intent, 101);


                    }
                }).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (resultCode == 0) {
                methodList = data.getStringArrayListExtra("methods");
                method_text.setText(methodList.size() + " selected");

                if(data.getStringExtra("class") != null){
                    class_text.setText(data.getStringExtra("class"));
                    jar_text.setText(data.getStringExtra("jar"));
                    jar = jar_text.getText().toString();
                    classname = class_text.getText().toString();
                    method_isChicked = true;
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jar = null;
        classname = null;
        methodname = null;
        class_isChicked = false;
        method_isChicked = false;


    }



//    private final class logConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.e("com.hjc.com.hjc.scripttool.com.hjc.scripttool", "logConnection");
//            iLogactService = ILogactService.Stub.asInterface(iBinder);
//            try {
//                iLogactService.startlogcat();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    }


}
