package com.hjc.scriptutil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by hujiachun on 15/11/2.
 */
public class FileNameFilter implements FilenameFilter {
    public String type;

    public FileNameFilter(String type){
        this.type = type;

    }



    @Override
    public boolean accept(File file, String s) {
        return s.endsWith(type);
    }
}