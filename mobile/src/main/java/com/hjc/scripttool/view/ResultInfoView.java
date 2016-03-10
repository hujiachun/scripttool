package com.hjc.scripttool.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjc.scripttool.R;
import com.hjc.scripttool.activity.MonkeyResultActivity;
import com.hjc.scriptutil.Appinfo;
import com.hjc.scriptutil.MRInfo;
import com.hjc.util.ShellUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hujiachun on 15/12/14.
 */
public class ResultInfoView extends BaseAdapter {
    ProgressDialog progress;
    private ArrayList<MRInfo> mrInfos;
    private int resource;
    private LayoutInflater inflater;
    Context context;
    ArrayList<String> crash, anr, fatal, traces_str;
    TextView historyView = null;


    public ResultInfoView(Context context, ArrayList<MRInfo> mrInfos, int resource) {
        this.context = context;
        this.mrInfos = mrInfos;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public int getCount() {
        return mrInfos.size();
    }

    @Override
    public MRInfo getItem(int i) {
        return mrInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView checkView = null;
        LinearLayout result;

        if(convertView == null){//缓存
            convertView = inflater.inflate(resource, null);
            historyView = (TextView) convertView.findViewById(R.id.time);
            result = (LinearLayout) convertView.findViewById(R.id.monkeyResult);
            checkView = (ImageView) convertView.findViewById(R.id.check);
            ViewCache cache = new ViewCache();
            cache.historyView = historyView;
            cache.checkView = checkView;
            cache.result = result;
            convertView.setTag(cache);
        }

        final ViewCache cache = (ViewCache) convertView.getTag();
        historyView = cache.historyView;
        checkView = cache.checkView;
        result = cache.result;
        historyView.setText(getItem(position).getHistory());
        final String clickHostory = getItem(position).getHistory();

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crash = new  ArrayList<>();
                anr = new  ArrayList<>();
                fatal = new  ArrayList<>();
                progress = ProgressDialog.show(context, "Loading...", "Please wait...", true, false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File("/sdcard/Result/Monkey/" + clickHostory + "/exec.txt");
                        if (file.isFile() && file.exists()) {
                            try {
                                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                                BufferedReader bufferedReader = new BufferedReader(read);
                                String lineTxt = null;
                                while ((lineTxt = bufferedReader.readLine()) != null) {
                                    if (lineTxt.contains("ANR in com.")) {
                                        anr.add(lineTxt);
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
                                                    ShellUtils.execCommand("cat /data/anr/" + traces_str.get(i) + " > " + "/sdcard/Result/Monkey/" + clickHostory +"/traces_" + i + ".txt", true);
                                                }
                                            }
                                        }).start();

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

                            handler.sendEmptyMessage(0);
                            Intent intent = new Intent(context.getApplicationContext(), MonkeyResultActivity.class);
                            intent.putExtra("anr", anr.size())
                                    .putExtra("fatal", fatal.size())
                                    .putExtra("crash", crash.size());
                            context.startActivity(intent);
                            crash = null;
                            anr = null;
                            fatal = null;


                        } else {
                            handler.sendEmptyMessage(0);
                            Looper.prepare();
                            Toast.makeText(context, "result not find", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();

            }
        });

        return convertView;
    }


        Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            progress.dismiss();// 关闭ProgressDialog
        }
    };


    private final class ViewCache{
        public TextView historyView;
        public ImageView checkView;
        public LinearLayout result;
    }
}
