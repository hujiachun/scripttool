package com.hjc.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by jinghao on 2014-09-10.
 */
public class Copier {

    public static void CopyAssets(Context context, String dir, String string) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(dir);
        } catch (IOException e) {

        }
        InputStream in;
        OutputStream out;
        if (files != null) {
            for (String file : files) {
                try {
                    in = assetManager.open(dir + "/" + file);
                    out = new FileOutputStream(string);
//                    byte[] buffer = new byte[1024];
//                    int read;
//                    while ((read = in.read(buffer)) != -1) {
//                        out.write(buffer, 0, read);
//                    }
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024 * 10];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void copyFileByStream(String in, String out) {

        try {
            InputStream inputStream = new FileInputStream(in);
            OutputStream outputStream = new FileOutputStream(out);
            copyFile(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(String src, String dest) {
        File test = new File(dest);
        if (!test.exists()) {
            test.mkdir();
        }
        Process p;
        try {

            p = Runtime.getRuntime().exec("cp " + src + " " + dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static boolean copyFileByStream(File src, File dst) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean copyFileByChannel(File src, File dest) {
        if (src.exists()) {
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            try {
                fi = new FileInputStream(src);
                fo = new FileOutputStream(dest);
                in = fi.getChannel();//得到对应的文件通道
                out = fo.getChannel();//得到对应的文件通道
                in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    assert fi != null;
                    assert in != null;
                    fi.close();
                    fo.close();
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        } else {

            return false;
        }

    }

}
