package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.hjc.scripttool.R;
import com.hjc.scripttool.view.ImageAdapter;
import com.hjc.util.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class GalleryActivity extends Activity {

    TextView testcase_text;
    public String shotString, testcase;
    public ArrayList<String> methods;
    public Gallery gallery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot);
        gallery = (Gallery) findViewById(R.id.gallery1);
        ImageView back = (ImageView) findViewById(R.id.back);
        LinearLayout left = (LinearLayout) findViewById(R.id.picLeft);
        LinearLayout right = (LinearLayout) findViewById(R.id.picRight);
        testcase_text = (TextView) findViewById(R.id.testcase);
        back.setImageResource(R.drawable.back3);
        Intent intent = getIntent();
        shotString = intent.getStringExtra(Constants.SHOT);
        testcase = intent.getStringExtra(Constants.TESTCASE);
        methods = intent.getStringArrayListExtra(Constants.CASE_LIST);
        testcase_text.setText(testcase);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                for (int i = 0; i < methods.size(); i++) {
                    if (methods.get(i).equals(testcase)) {
                        index = i;
                    }
                }
                if (index > 0) {
                    gallery.setAdapter(new ImageAdapter(getApplicationContext(), shotString, methods.get(index - 1), methods));
                    testcase = methods.get(index - 1);
                    testcase_text.setText(testcase);
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                for (int i = 0; i < methods.size(); i++) {
                    if (methods.get(i).equals(testcase)) {
                        index = i;
                    }
                }
                if (index < methods.size() - 1) {
                    gallery.setAdapter(new ImageAdapter(getApplicationContext(), shotString, methods.get(index + 1), methods));
                    testcase = methods.get(index + 1);
                    testcase_text.setText(testcase);
                }
            }
        });



        //将存放图片的ImageAdapter给gallery对象
        gallery.setAdapter(new ImageAdapter(this, shotString, testcase, methods));

        //设置Gallery的监听事件
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(GalleryActivity.this, testcase, Toast.LENGTH_SHORT).show();
            }
        });
    }



}