package com.hjc.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import android.content.Context;
import android.util.Log;
import com.hjc.util.Constants;
import org.apache.http.params.CoreConnectionPNames;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class Http {
    //real time post
    private String m_case;

    /**
     * real time post result
     *
     * @param context      context to read preferences
     * @param m_case       case json string
     * @param m_module     module of cases
     * @param m_screenshot screenshot of case
     */
    public Http(Context context, String m_case, long m_module, File m_screenshot) {

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
            }
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
        int status = -1;

        return status;
    }

    public int postRealTimeResult() {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

        String url = "";
        HttpPost httppost = new HttpPost(url);
        Log.e(Constants.TAG, "Upload url : " + url);

        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        MultipartEntityBuilder me = MultipartEntityBuilder.create();
        me.addTextBody("case", m_case, contentType);

        HttpEntity httpEntity = me.build();
        httppost.setEntity(httpEntity);
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
               Log.e(Constants.TAG, line);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, e.toString());
            return -1;
        }
        return 0;
    }

}
