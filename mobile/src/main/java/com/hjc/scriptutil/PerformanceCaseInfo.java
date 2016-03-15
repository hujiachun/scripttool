package com.hjc.scriptutil;

import java.util.ArrayList;

/**
 * Created by hujiachun on 16/3/8.
 */
public class PerformanceCaseInfo {
    private String caseName;
    private Float maxMem;
    private Float averageMem;
    private double maxCpu;
    private double averageCpu;
    private PerformaceData performaceData;

    public PerformaceData getPerformaceData() {
        return performaceData;
    }

    public void setPerformaceData(PerformaceData performaceDatat) {
        this.performaceData = performaceDatat;
    }

    public Float getMaxMem() {
        return maxMem;
    }

    public void setMaxMem(Float maxMem) {
        this.maxMem = maxMem;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public Float getAverageMem() {
        return averageMem;
    }

    public void setAverageMem(Float averageMem) {
        this.averageMem = averageMem;
    }

    public double getMaxCpu() {
        return maxCpu;
    }

    public void setMaxCpu(double maxCpu) {
        this.maxCpu = maxCpu;
    }

    public double getAverageCpu() {
        return averageCpu;
    }

    public void setAverageCpu(double averageCpu) {
        this.averageCpu = averageCpu;
    }
}
