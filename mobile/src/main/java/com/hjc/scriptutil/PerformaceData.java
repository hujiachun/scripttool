package com.hjc.scriptutil;

import android.content.Context;

import com.hjc.util.Constants;
import com.hjc.util.Util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by hujiachun on 16/3/8.
 */
public class PerformaceData {

    private DecimalFormat dFormat;
    public float maxMem;
    public double maxCpu;
    public float averageMem;
    private double averageCpu;
    public ArrayList<String> dataList;
    public ArrayList<Float> memMax, memFloatList;
    public ArrayList<Double> cpuMax;
    public ArrayList<Double> cpuDoubleList;


    public PerformaceData(String path, Context context) throws IOException {
        dFormat = new DecimalFormat("0.00");
        this.dataList = Util.readCsv(path, context.getApplicationContext());//获取行数据
        memFloatList = new ArrayList();
        cpuDoubleList = new ArrayList();
        for( String data : dataList){
            memFloatList.add(Float.parseFloat(data.split(",")[2]));//得到内存
            if(!data.contains(Constants.NA)){
                cpuDoubleList.add(Double.parseDouble(data.split(",")[4].split(Constants.PCT)[0]));
            }

        }
    }

    public float getMaxMem() {
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
        float n = sum / memFloatList.size();
        averageMem = Float.parseFloat(dFormat.format(n));
        return averageMem;
    }

    public double getMaxCpu() {
        maxCpuVaule();//获取数组最大Cpu
        return maxCpu;
    }

    private void maxCpuVaule() {
        cpuMax = new ArrayList<>();
        cpuMax.addAll(cpuDoubleList);
        Collections.sort(cpuMax);
        maxCpu = cpuMax.get(cpuMax.size() - 1);

    }

    public double getAverageCpu() {
        double sum = 0;
        for(int i = 0; i < cpuDoubleList.size(); i++){
            sum = sum + cpuDoubleList.get(i);
        }
        double n = sum / cpuDoubleList.size();
        averageCpu = Double.parseDouble(dFormat.format(n));
        return averageCpu;
    }

}
