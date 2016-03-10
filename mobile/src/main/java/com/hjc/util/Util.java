package com.hjc.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ILogactService;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hjc.scripttool.R;
import com.hjc.scriptutil.Settings;
import com.hjc.service.LogcatService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by hujiachun on 15/11/3.
 */
public class Util {
    public static ILogactService iLogactService;

    public static int getPid(String tag) throws IOException {
        Process p;
        p = Runtime.getRuntime().exec("ps ");
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = bufferedReader2.readLine()) != null) {
            if (line.contains(tag)) {
                Log.e(Constants.TAG, line);
                if (line.split("\\s")[0].equals("shell")) {
                    return Integer.parseInt(line.split("\\s")[5]);
                } else if (line.split("\\s")[0].equals("system")) {
                    return Integer.parseInt(line.split("\\s")[4]);
                } else if(line.split("\\s")[0].contains("u")){
                    return Integer.parseInt(line.split("\\s+")[1]);
                }
                else {
                    return Integer.parseInt(line.split("\\s")[6]);
                }
            }
        }
        bufferedReader2.close();
        return 0;
    }

    /**
     * upgrade app to get root permission
     *
     * @return is root successfully
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); // 切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            int existValue = process.waitFor();
            if (existValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "upgradeRootPermission exception=" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }




    /**
     * 获取logcat pid
     * @param tag
     * @return
     * @throws IOException
     */
    public static int getlogPid(String tag) throws IOException {
        Process p;
        p = Runtime.getRuntime().exec("ps |grep " + tag);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains(tag)) {
                Log.e(Constants.TAG, line);
                if (line.split("\\s")[0].equals("root")) {
                    Log.e(Constants.TAG, tag + " pid :" + line);
                    if((line.split("root      ")[1].split("  ")[0]).contains(" ")){
                        return Integer.parseInt(line.split("root      ")[1].split("  ")[0].split(" ")[0]);
                    }
                    else{
                        return Integer.parseInt(line.split("root      ")[1].split("  ")[0]);
                    }
                }
                else {
                    Log.e(Constants.TAG, "not find logcat for root");
                }
            }
        }
        bufferedReader.close();
        return 0;
    }

    public static void createResult(File file, BufferedReader reader) throws IOException {

        File fi = new File(file.getCanonicalFile() + "/log.txt");
        FileOutputStream fos = new FileOutputStream(fi, true);
        String read;
        while ((read = reader.readLine()) != null){
            fos.write(read.getBytes());
            fos.write("\n".getBytes());//换行
        }
        fos.close();
        reader.close();


    }


    public static ArrayList<String>  getPackage() throws IOException, InterruptedException {

        ArrayList<String> packageStr = new ArrayList<String>();
        BufferedReader reader = ShellUtils.execCcommand("pm list packages -u -3");

        String line;
        while ((line = reader.readLine()) != null) {
            String packageNameStr = line.split(":")[1];
            packageStr.add(packageNameStr);

        }
        reader.close();

        return packageStr;

    }

    /**
     * Csv
     * @param filePath
     */
    public static ArrayList<String> readCsv(String filePath, Context context) throws IOException {
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        ArrayList<String> date = new ArrayList<>();
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                 read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                 bufferedReader = new BufferedReader(read);
                 String lineTxt = null;
                 int number = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if(number > 5){
                        date.add(lineTxt);
                    }
                    number++;
                }
                read.close();
                bufferedReader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(read != null){
                    read.close();
                }
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            }

        } else {
            Toast.makeText(context , "result not find", Toast.LENGTH_SHORT).show();
        }
        return date;
    }


    /**
     * 读取text结果
     * @param filePath
     */
    public static ArrayList<String> readText(String filePath, Context context) {
        ArrayList<String> testcases = new ArrayList<String>();
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    testcases.add(lineTxt);
                }
                read.close();
                bufferedReader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context , "result not find", Toast.LENGTH_SHORT).show();
        }
        return testcases;
    }


    public static boolean deleteDir(File dir) {
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

    /**
     * 返回桌面
     * @param context
     */
    public static void pressHome(Context context){
        Intent intent= new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);

    }


    /**
     * 启动logcat服务
     */
    public static void startLogcat(Context context, File file){
//        SharedPreferences log = context.getSharedPreferences("logcatpath", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor logeditor = log.edit();
//        logeditor.clear();
//        logeditor.putString("resultpath", file.getAbsolutePath());
//        logeditor.commit();

        Settings.getDefaultSharedPreferences(context).edit().putString(Settings.KEY_LOGCAT_PATH, file.getAbsolutePath()).commit();
        logConnection conn = new logConnection();
        Intent intent = new Intent(context, LogcatService.class);
        context.bindService(intent, conn, context.BIND_AUTO_CREATE);

    }


    /**
     * 停止logCat
     * @param context
     */
    public static void stopLogCat(Context context){
        try {
            int pid = Util.getlogPid(Constants.LOGCAT);
            if (pid != 0) {
                ShellUtils.execCommand("kill " + pid, true);
                Settings.getDefaultSharedPreferences(context).edit().putBoolean(Settings.KEY_LOGCAT_STATE, false).commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final class logConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(Constants.TAG, "logConnection");
            iLogactService = ILogactService.Stub.asInterface(iBinder);
            try {
                iLogactService.startlogcat();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }


    /**
     * 用来判断服务是否运行.
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
           ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
            }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
               isRunning = true;
                break;
               }
           }
        return isRunning;
    }





}
