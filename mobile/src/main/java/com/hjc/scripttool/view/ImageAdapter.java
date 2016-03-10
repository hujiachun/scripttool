package com.hjc.scripttool.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.hjc.scripttool.R;

import java.io.File;
import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter{

    private String[] picList;
    private Context mContext;
    public String shotString, testcase;
    public ArrayList<String> methods;



    //构造
    public ImageAdapter(Context context, String shotString, String testcase, ArrayList methods){
        this.shotString = shotString;
        mContext = context;
        picList = new File(shotString).list();
        this.testcase = testcase;
        this.methods = methods;
        initData();
    }


    private void initData(){
//        for(String method : methods){
//            Log.e("scripttool", method);
//        }

    }


    //获取图片的个数
    public int getCount() {
        // TODO Auto-generated method stub
        return picList.length;
    }

    //获取图片在库中的位置
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    //获取图片在库中的ID
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    //将图片取出来
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(getScreenshotPicByTestCaseName(shotString, testcase));
        imageView.setLayoutParams(new Gallery.LayoutParams(2574, 1404));


        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }

    private Bitmap getScreenshotPicByTestCaseName(String shotString, String testcase) {

        File picFile = new File(shotString + "/" + testcase + ".png");
        if (picFile.exists()) {
            Bitmap screenShot = BitmapFactory.decodeFile(picFile.getAbsolutePath());
            return screenShot;
        }

        return null;
    }




    /**

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initDate() {
    Cursor cursor =  mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    while (cursor.moveToNext()){
    //得到图片的名字
    String name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
    //得到图片的描述信息
    String info = cursor.getString(cursor.getColumnIndex(Media.DESCRIPTION));
    //图片数据
    byte[] data = cursor.getBlob(cursor.getColumnIndex(Media.DATA));
    HashMap map = new HashMap();
    map.put("name", name==null ? "":name);
    map.put("info", info==null ? "":info);
    map.put("data", data);
    picList.add(map);
    }
    cursor.close();

    }

    public ImageAdapter(Context c, int resource){
    inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.resource = resource;
    mContext = c;
    initDate();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = null;
        if(convertView==null){
            convertView = inflater.inflate(resource, null);
            image = (ImageView) convertView.findViewById(R.id.gallery1);
            ViewCache cache = new ViewCache();
            cache.image = image;
            convertView.setTag(cache);

        }
        ViewCache cache = (ViewCache) convertView.getTag();
        image = cache.image;
        byte[] bs = (byte[]) picList.get(position).get("data");

        //设置图片
        Bitmap bitmap = BitmapFactory.decodeFile(new String(bs, 0, bs.length - 1));
        image.setImageBitmap(bitmap);
        //设置标识符
        image.setTag(position);
        list.add(name);


        return convertView;
    }


    public final class ViewCache{

        public ImageView image;

    }

    **/
}