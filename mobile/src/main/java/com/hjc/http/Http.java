package com.hjc.http;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hjc.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class Http {





    public static final String PROTOCOL_HTTP = "";

    public static final String PROTOCOL_HTTPS = "";

    public static final String UIA_RESULT_URL = "";

    public static final String UIA_UPLOAD_URL = "";

    public static final String STRESS_UPLOAD_URL = "";

    private static final String IMG_URL = "";

    private static final String STORAGE_URL = "";


    //every post need these.
    private String m_device;
    private String m_version;

    //real time post
    private String m_case;

    //case need a module
    private long m_module;

    private File m_screenshot;

    private SharedPreferences pref_sk;

    public Http(Context context) {

    }



    public static long getContentLength(String url) throws IOException {
        HttpGet post = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        return client.execute(post).getEntity().getContentLength();
    }

    public int upload(String path, String url) {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        HttpPost httppost = new HttpPost(url);

        File file = new File(path);
        MultipartEntityBuilder me = MultipartEntityBuilder.create();
        me.addBinaryBody("m_file", file);
        HttpEntity httpEntity = me.build();
        httppost.setEntity(httpEntity);
        HttpResponse response;
        String result = "";
        String line = "";
        try {
            response = httpclient.execute(httppost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((line = rd.readLine())!= null) {
                result += line;
                Log.e(Constants.TAG, result);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "upload failed because of :\n" + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        int status = -1;
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }


}
