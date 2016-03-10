package com.hjc.scriptutil;

import android.content.Context;
import android.util.Log;

import com.hjc.util.Constants;
import com.hjc.util.Util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hujiachun on 16/3/8.
 */
public class PerformaceData {

    public float maxMem;
    public float averageMem;
    public ArrayList<String> dataList;
    public ArrayList<Float> memMax, memFloatList;

    public PerformaceData(String path, Context context) throws IOException {
        this.dataList = Util.readCsv(path, context.getApplicationContext());//获取行数据
        memFloatList = new ArrayList();

    }




    public float getMaxMem() {

        for( String data : dataList){//得到内存
            memFloatList.add(Float.parseFloat(data.split(",")[2]));
        }
        maxVaule();//获取数组最大内存
        return maxMem;
    }

    public void maxVaule() {
        memMax = new ArrayList<>();
        memMax.addAll(memFloatList);
        Collections.sort(memMax);
        maxMem = memMax.get(memMax.size() - 1);
    }

    public float getAverageMem() {
        float sum = 0;
        for(int i = 0; i < memFloatList.size(); i++){
            sum = sum + memFloatList.get(i);
        }

        DecimalFormat dFormat=new DecimalFormat("0.00");
        float n = sum / memFloatList.size();
        averageMem = Float.parseFloat(dFormat.format(n));

//        Log.e(Constants.TAG, sum + ", " + memFloatList.size() + ", " + averageMem);
        return averageMem;
    }
}
