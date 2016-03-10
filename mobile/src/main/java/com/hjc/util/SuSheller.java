package com.hjc.util;



import java.io.File;
import java.io.IOException;


public class SuSheller {

    public static boolean clearTestCache(boolean isDev) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("su");
            //chmod 777 /data/local/tmp
            p.getOutputStream().write(("chmod 777 /data/" + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("chown system:system /data/" + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("chmod 777 /data/local/" + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("chown system:system /data/local/" + "\n").getBytes());
            p.getOutputStream().flush();
            if (!new File("/data/local/tmp/").exists()) {
                p.getOutputStream().write(("mkdir " + "/data/local/tmp/" + "\n").getBytes());
                p.getOutputStream().flush();
            }
            p.getOutputStream().write(("chmod 777 /data/local/tmp/" + "\n").getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write(("chown system:system /data/local/tmp/" + "\n").getBytes());
            p.getOutputStream().flush();
            if (!isDev) {
                File dilvik = new File("/data/local/tmp/" + "dalvik-cache");
                if (!dilvik.exists()) {
                    try {
                        p.getOutputStream().write(("mkdir " + dilvik.getPath() + "\n").getBytes());
                        p.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                p.getOutputStream().write(("chmod 777 " + dilvik.getPath() + "\n").getBytes());
                p.getOutputStream().flush();
                p.getOutputStream().write(("chown system:system " + dilvik.getPath() + "\n").getBytes());
                p.getOutputStream().flush();
            }
            p.getOutputStream().write(("exit\n").getBytes());
            p.getOutputStream().flush();
            p.waitFor();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
