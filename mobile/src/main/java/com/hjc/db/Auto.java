package com.hjc.db;

import java.io.File;

/**
 * Created by hujiachun on 15/12/11.
 */
public class Auto {
    private  Integer id;
    private  String type;
    private  String testcase;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestcase() {
        return testcase;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }

    public String getType() {
        return type;
    }

    public Auto(Integer id, String testcase, String type) {
        this.id = id;
        this.testcase = testcase;
        this.type = type;
    }
    public Auto() {

    }
    public void setType(String type) {
        this.type = type;
    }
}
