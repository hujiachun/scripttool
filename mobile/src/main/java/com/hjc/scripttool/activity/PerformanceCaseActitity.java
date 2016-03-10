package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.hjc.scripttool.R;
import com.hjc.scripttool.view.CaseAdapter;
import com.hjc.scriptutil.PerformaceData;
import com.hjc.scriptutil.PerformanceCaseInfo;
import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by hujiachun on 16/3/8.
 */
public class PerformanceCaseActitity extends Activity{
    ArrayList<PerformanceCaseInfo> items = new ArrayList<>();
    public ArrayList<String> case_list;
    public SharedPreferences perference;
    public RecyclerView recyclerView;
    public HashMap<String, Float> max_mem, average_mem;
    public HashMap<String, HashMap> max_mem_hash, average_mem_hash;


    private void init() {

        for(int i = 0; i < case_list.size(); i++){
            PerformanceCaseInfo performanceCaseInfo = new PerformanceCaseInfo();
            String case_str = case_list.get(i).split(Constants.CSV)[0];
            performanceCaseInfo.setCaseName(case_str);//获取用例名称
            performanceCaseInfo.setMaxMem((Float) max_mem_hash.get(case_str).get(Constants.CASE_NAME));
            performanceCaseInfo.setAverageMem((Float) average_mem_hash.get(case_str).get(Constants.CASE_NAME));

            items.add( performanceCaseInfo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        max_mem_hash = new HashMap<>();
        average_mem_hash = new HashMap<>();
        String path = Constants.PERFORMANCE_PATH + getIntent().getStringExtra(Constants.TYPE)
                + "_"  + getIntent().getStringExtra(Constants.TIME) + "/";
        case_list = getIntent().getStringArrayListExtra(Constants.CASE_LIST);
        perference = Settings.getDefaultSharedPreferences(getApplicationContext());
                for(int i = 0; i < case_list.size(); i++){
                    String case_name = case_list.get(i).split(Constants.CSV)[0];
                    max_mem = new HashMap();
                    average_mem = new HashMap();
                    try {
                        PerformaceData pd = new PerformaceData(path + case_list.get(i), getApplicationContext());
                        max_mem.put(Constants.CASE_NAME, pd.getMaxMem());
                        average_mem.put(Constants.CASE_NAME, pd.getAverageMem());
                        max_mem_hash.put(case_name, max_mem);//case 对应相应的值
                        average_mem_hash.put(case_name, average_mem);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        init();
        setContentView(R.layout.performancelist);
        recyclerView = (RecyclerView) findViewById(R.id.performance_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final CaseAdapter adapter = new CaseAdapter(items, path, getApplicationContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            Paint paint = new Paint();
            @Override
            public void onDraw(Canvas c, RecyclerView parent,
                               RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent,
                                   RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                paint.setColor(Color.LTGRAY);
                for (int i = 0, size = parent.getChildCount(); i < size; i++) {
                    View child = parent.getChildAt(i);
                    c.drawLine(child.getLeft(), child.getBottom(),
                            child.getRight(), child.getBottom(), paint);
                }
            }

        });
        //可以提高效率
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Constants.TAG, "onDestroy");
    }
}
