package com.hjc.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hjc.scriptutil.LeadJarServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by hujiachun on 15/11/2.
 */
public class TestCase  extends AndroidTestCase {


    public void test001() throws IOException {
        List<String> classes = LeadJarServices.getAllClasses(getContext(), new File("/sdcard/clock.jar"));
        for (String classe : classes) {
            Log.v("TestCase", classe);
        }
    }

    public void test002() throws IOException, ClassNotFoundException {
        List<String> methods = LeadJarServices.addTestClassesFromJars(getContext(), "/sdcard/clock.jar", "com.meizu.clock.com.hjc.scripttool.ClockSanityTestNewCase");
        for (String method : methods) {
            Log.v("TestCase", method);
        }


    }


    public void test003() throws IOException, InterruptedException {
        readText("/sdcard/Result/Uiautomator/1449574053573/clock/result.txt");
    }


    public void readText(String filePath){
        File file= new File(filePath);
        if(file.isFile() && file.exists()){
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    Log.e("Test", lineTxt);
                }
                read.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            Log.e("Test", "文件不存在");
        }

    }

}