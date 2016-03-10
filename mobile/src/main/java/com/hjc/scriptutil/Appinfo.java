package com.hjc.scriptutil;

import android.graphics.drawable.Drawable;
import java.io.Serializable;

/**
 * Created by hujiachun on 15/11/13.
 */
public class Appinfo implements Serializable {
    private static final long serialVersionUID = 1L; // 定义序列化ID
    private String name; //应用名
    private String packageName; //包名
    private Drawable icon; //应用图标

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }



}
