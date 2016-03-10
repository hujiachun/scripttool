package com.hjc.scriptutil;

import android.content.Context;
import android.os.Environment;

import com.hjc.util.ShellUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by hujiachun on 15/11/2.
 */
public class LeadJarServices {


    /**
     * 加载Jar包
     * @return
     */
    public static List<String> getJarList() {
        List<String> list = new ArrayList<String>();
        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File SDFile = Environment.getExternalStorageDirectory();
                File sdPath = new File(SDFile.getAbsolutePath());
                FileNameFilter name_filter = new FileNameFilter(".jar");//创建过滤器

                if (sdPath.listFiles().length > 0) {
                    for (File file : sdPath.listFiles()) {
                  if (name_filter.accept(sdPath, file.getName())) {
                            list.add(file.getName());
                        }
                    }
                }
            } catch (Exception e) {

            }
        }

        return  list;
    }

    /**
     * 加载Jar包
     * @return
     */
    public static List<String> getSystemJarList() {

        List<String> list = new ArrayList<String>();
                File sdPath = new File("/data/local/tmp/");
                FileNameFilter name_filter = new FileNameFilter(".jar");//创建过滤器

                if (sdPath.listFiles().length > 0) {
                    for (File file : sdPath.listFiles()) {
                        if (name_filter.accept(sdPath, file.getName())) {
                            list.add(file.getName());
                        }
                    }
                }

        return  list;

    }



    /**
     * 读取jar文件
     * @param file
     * @throws IOException
     */
    public static List<String> getAllClasses(Context context, File file) throws IOException {
        List<String> classes = new ArrayList<String>();
        DexFile dx = DexFile.loadDex(file.getPath(), File.createTempFile("opt", "dex", context.getCacheDir()).getPath(), 0);
        for(Enumeration<String> classNames = dx.entries(); classNames.hasMoreElements();) {
            String className = classNames.nextElement();

            if (className.contains("Sanity")){
                classes.add(className);
            }

        }
        return classes;
    }


    /**
     * 加载方法
     * @param context
     * @param jarPath
     * @param testCase
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> addTestClassesFromJars(Context context, String jarPath, String testCase) throws ClassNotFoundException {
        String dexPath = jarPath + File.pathSeparator + "/system/framework/android.test.runner.jar" + File.pathSeparator + "/system/framework/uiautomator.jar";
        String dexOutputDir = context.getApplicationInfo().dataDir;
        DexClassLoader classLoader = new DexClassLoader(dexPath, dexOutputDir, null, context.getClass().getClassLoader());
        List<String> caseList = new ArrayList<>();

        Class cls = classLoader.loadClass(testCase);
        Method[] methods = cls.getMethods();
        for (Method m : methods) {
            if(m.getName().startsWith("test")){
                caseList.add(m.getName());
            }
        }
        return caseList;
    }
}
