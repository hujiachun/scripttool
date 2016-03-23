package com.hjc.scripttool.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjc.scripttool.R;
import com.hjc.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hujiachun on 16/3/22.
 */
public class TestActivity extends Activity{
    Bitmap bm = null;
    ImageView  image;
    Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] imageByte = msg.getData().getByteArray("data");

            bm = BitmapFactory.decodeByteArray(imageByte, 0 , imageByte.length);
            image.setImageBitmap(bm);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        TextView  text = (TextView) this.findViewById(R.id.text);
        image = (ImageView) this.findViewById(R.id.imageview);
        final String url = text.getText().toString();
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[]  imageByte = getImage(url);
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("data", imageByte);
                            Message msg = new Message();
                            msg.setData(bundle);
                            hander.sendMessage(msg);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(Constants.TAG, e.toString());
                        }
                    }
                }).start();


            }
        });


    }

    public byte[] getImage(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
         return getByte(conn.getInputStream());
        }
        return null;
    }

    private byte[] getByte(InputStream inputStream) throws IOException {
        byte[] outByte;
        ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ( (len = inputStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }

        outByte = outStream.toByteArray();
        inputStream.close();
        outStream.close();
        return outByte;
    }


}
