package com.hjc.util;

import android.util.Log;
import java.io.*;
import java.util.Date;

public class Writer {

    public static String newExcelPath;

    public static boolean chmodEverything(String path) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("su");
            p.getOutputStream().write(("chmod 777 " + path + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("exit\n").getBytes());
            p.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public static boolean chmodEverythingBySystem(String path) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("chmod -R 777 " + path);
            p.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

        return true;
    }





    public static boolean logParsing(String tag) {
        Process p = null;
        BufferedReader reader = null;
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String testLog = "logcat -v time";
        try {
            p = Runtime.getRuntime().exec(testLog);
            reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            stringBuilder.setLength(0);
            Date wifiTime = new Date(System.currentTimeMillis() + 10000);
            while (((line = reader.readLine()) != null)
                    && wifiTime.after(new Date(System.currentTimeMillis()))) {
                stringBuilder.append(line);
                if (line.matches("^.*" + tag + ".*$")) {
                    System.out.println(line);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        destroy(p, reader);
        return false;
    }





    public static void writeResult(File file, String text, boolean saveOld) throws IOException {
        if (!saveOld) {
            file.delete();
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.newLine();
        bw.close();
        fw.close();
    }


    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static boolean rmFile(String path) {
        Process p;
        try {
            String command1 = "su rm " + path;
            p = Runtime.getRuntime().exec(command1);


        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
        try {
            assert p != null;
            p.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static boolean rmDir(String path) {
        Process p;
        try {
            String command1 = "su";
            p = Runtime.getRuntime().exec(command1);
            p.getOutputStream().write(("rm -rf " + path + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("exit\n").getBytes());

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            if (file.delete()) {
                flag = true;
            } else {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (File file : files) {
            //删除子文件
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }

    public static void destroy(Process p, BufferedReader reader) {
        p.destroy();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLine(File file, String text, boolean isAppend){
        try {
            FileWriter fw = new FileWriter(file, isAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
