package com.hjc.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hjc.performance.MemoryInfo;
import com.hjc.scripttool.R;
import com.hjc.scriptutil.Programe;
import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import com.hjc.util.ProcessInfo;
import com.hjc.util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hujiachun on 16/2/25.
 */
public class PerformanceService extends Service{

    private DecimalFormat fomart;
    private Handler handler = new Handler();
    private int delaytime, pid;
    private MemoryInfo memoryInfo;
    private String package_name;
    private double freeMemoryKb, processMemory;
    private String[][] heap;
    private boolean performanceService, heapState;
    private SharedPreferences preferences;
    public static String resultFilePath;
    public static BufferedWriter bw;
    public static FileOutputStream fos;
    public static OutputStreamWriter osw;
    private static final String BLANK_STRING = "";
    private int index = 0;
    public  String test_case;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        fomart = new DecimalFormat();
        memoryInfo = new MemoryInfo();
        preferences = Settings.getDefaultSharedPreferences(getApplicationContext());
        heap = new String[2][2];
        delaytime = preferences.getInt(Settings.KEY_INTERVAL, 5);

        heapState = preferences.getBoolean(Settings.KEY_HEAP_STATE, false);
        package_name = preferences.getString(Settings.KEY_PACKAGE, Constants.DEFAULT_PACKAGE).split(":")[1];
        test_case =  preferences.getString(Settings.KEY_CASE, Constants.PERFORMANCE);
        Programe programe = new ProcessInfo().getProgrameByPackageName(getApplicationContext(), package_name);
        if(programe.getPid() != 0){
            pid = programe.getPid();
        }
        else {
            try {
                pid = Util.getPid(package_name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e(Constants.TAG, programe.getProcessName() + " = " + pid);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createResultCsv(test_case);
        handler.postDelayed(task, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {

            try {
                dataRefresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.e(Constants.TAG, "freeMemoryKb: " + freeMemoryKb + " ; " + "processMemory:" + processMemory);
//            Log.e(Constants.TAG, "Native Heap-Size:" + heap[0][0] + "; Native Heap-Alloc:" + heap[0][1] + "; Dalvik Heap-Size:" + heap[1][0] + " ;Dalvik Heap-Alloc:" + heap[1][1]);
            handler.postDelayed(this, delaytime * 1000);
        }
    };


    /**
     * refresh the performance data showing in floating window.
     * @throws FileNotFoundException
     *
     * @throws IOException
     */
    private void dataRefresh() throws IOException {
        Date date = new Date(new Date().getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String str = format.format(date);
        int pidMemory = memoryInfo.getPidMemorySize(pid, getApplicationContext());
        long freeMemory = memoryInfo.getFreeMemorySize(getApplicationContext());
        freeMemoryKb = (double) freeMemory / 1024;
        processMemory = (double) pidMemory / 1024;

        if(heapState){
            heap = memoryInfo.getHeapSize(pid, getApplicationContext());
        }
        DecimalFormat dFormat=new DecimalFormat("0.00");
        String percent = dFormat.format((processMemory / freeMemoryKb) * 100);
        bw.write(index++ + Constants.COMMA + str + Constants.COMMA + dFormat.format(processMemory) + Constants.COMMA + percent + Constants.LINE_END);
        bw.flush();
    }


    private void createResultCsv(String testcase) {
//        Date date = new Date(new Date().getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        String str = format.format(date);
        resultFilePath = "/sdcard/Result/performance/" + preferences.getString(Settings.KEY_PACKAGE, "").split(":")[0]
                + "_" + preferences.getString(Settings.KEY_TIME, Constants.NA);


        try {

            File file = new File(resultFilePath);
            if(!file.exists()){
                file.mkdirs();
            }

            File resultFile = new File(file.getAbsolutePath() + "/"+ testcase + ".csv");
            resultFile.createNewFile();

            fos = new FileOutputStream(resultFile);
            osw = new OutputStreamWriter(fos, "GBK");
            bw = new BufferedWriter(osw);
            long totalMemorySize = memoryInfo.getTotalMemory();
            String totalMemory = fomart.format((double) totalMemorySize / 1024);

            //基本信息
            bw.write(getString(R.string.process_package) + Constants.COMMA + package_name + Constants.LINE_END +  getString(R.string.process_pid) + Constants.COMMA + pid
                    + Constants.LINE_END + getString(R.string.mem_size) + Constants.COMMA + totalMemory + "MB" + Constants.LINE_END
                    + getString(R.string.android_system_version) + Constants.COMMA + memoryInfo.getSDKVersion() + Constants.LINE_END
                    + getString(R.string.mobile_type) + Constants.COMMA + memoryInfo.getPhoneType() + Constants.LINE_END);
            //横
            bw.write(getString(R.string.index) + Constants.COMMA + getString(R.string.timestamp) + Constants.COMMA + getString(R.string.used_mem_PSS) + Constants.COMMA + getString(R.string.used_mem_ratio) + Constants.LINE_END);
            bw.flush();

        } catch (IOException e) {
            Log.e(Constants.TAG, e.getMessage());
        }
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(task);
        super.onDestroy();
    }
}
