package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.hjc.scripttool.R;
import com.hjc.scripttool.view.PerformanceAdapter;
import com.hjc.scriptutil.Performanceinfo;
import com.hjc.util.Constants;
import com.hjc.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hujiachun on 16/3/2.
 */
public class PerformanceHistoryAcitity extends Activity{
    ArrayList<Performanceinfo> items = new ArrayList<>();
    private ArrayList<String> history_list;


    private  void init(){
        history_list = getIntent().getStringArrayListExtra(Constants.PERFORMANCE_LIST);
        for(int i = 0; i < history_list.size(); i++){
            Performanceinfo performance = new Performanceinfo();
            performance.setName(history_list.get(i).split("_")[0]);
            performance.setTime(history_list.get(i).split("_")[1]);
            performance.setDelete_img(R.drawable.item_delete);
            performance.setRereview_img(R.drawable.ic_item_review);
            performance.setName_img(R.drawable.task_path);
            performance.setTime_img(R.drawable.task_time);
            items.add(performance);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performancelist);
        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.performance_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final PerformanceAdapter adapter = new PerformanceAdapter(items, getApplicationContext());
        adapter.setListener(new PerformanceAdapter.OnRecyclerViewItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {

           }

            @Override
            public void onItemLongClick(View view, int position) {
                synchronized (this) {
                    Util.deleteDir(new File(Constants.PERFORMANCE_PATH + items.get(position).getName() + "_" + items.get(position).getTime()));
                    items.remove(position);
                    adapter.notifyItemRemoved(position);
                    notifyAll();

                }
            }
        });

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


}

