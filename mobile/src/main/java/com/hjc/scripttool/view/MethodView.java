package com.hjc.scripttool.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjc.scripttool.R;
import com.hjc.scripttool.activity.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hujiachun on 15/11/18.
 */
public class MethodView extends BaseAdapter{
    public static ArrayList<String> isSelected_methodname = null;
    public ArrayList<String> methods, error_methods;
    public int resource;
    public LayoutInflater inflater;
    public static HashMap<Integer, Boolean> isSelected;
    public Context context;
    public File shotfile;

    public MethodView(Context context, ArrayList<String> methods, int resource, File file, ArrayList<String> error_methods) {
        shotfile = file;
        this.context = context;
        this.methods = methods;
        this.error_methods = error_methods;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isSelected = new HashMap<Integer, Boolean>();
        isSelected_methodname = new ArrayList<>();
        // 初始化数据
        initDate();

    }

    public  static ArrayList<String> getIsSelected_methodname(){
        return isSelected_methodname;
    }

    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    private void initDate() {

        for (int i = 0; i < methods.size(); i++) {
            getIsSelected().put(i, false);
        }

    }

    @Override
    public int getCount() {
        return methods.size();
    }

    @Override
    public Object getItem(int i) {
        return methods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        TextView methodView = null, index = null;
        CheckBox checkBox = null;
        ImageView reviewPic = null, state = null;
        LinearLayout review = null;
        if(view == null){
            view = inflater.inflate(resource, null);

            index = (TextView) view.findViewById(R.id.method_index);
            methodView = (TextView) view.findViewById(R.id.methodname);
            checkBox = (CheckBox) view.findViewById(R.id.methodcheck);
            reviewPic = (ImageView) view.findViewById(R.id.reviewPic);
            review = (LinearLayout) view.findViewById(R.id.review);
            state = (ImageView) view.findViewById(R.id.testcase_state);
            ViewCache cache = new ViewCache();
            cache.index = index;
            cache.methodView = methodView;
            cache.checkBox = checkBox;
            cache.reviewPic = reviewPic;
            cache.state = state;
            cache.review = review;
            view.setTag(cache);
        }

        ViewCache cache = (ViewCache) view.getTag();
        index = cache.index;
        methodView = cache.methodView;
        checkBox = cache.checkBox;
        reviewPic = cache.reviewPic;
        review = cache.review;
        state = cache.state;
        index.setText(i + 1 + ".");


        methodView.setText(methods.get(i));
        if(error_methods != null){
            reviewPic.setVisibility(View.VISIBLE);
            if(error_methods.contains(methods.get(i))){
                state.setImageResource(R.drawable.error5);
                methodView.setTextColor(0xffff0000);
            }
            else {
                state.setImageResource(R.drawable.ok);
                methodView.setTextColor(0xFF000000);
            }
        }

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("shotString", shotfile.getAbsolutePath()).putExtra("testcase", methods.get(i)).putStringArrayListExtra("caselist", methods);
                intent.setClass(context.getApplicationContext(), GalleryActivity.class);
                context.startActivity(intent);
            }
        });


        /**
         * textView绑定监听checkbox
         */
        final CheckBox finalCheckBox = checkBox;
        methodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSelected.get(i)) {
                    isSelected.put(i, false);
                    finalCheckBox.setChecked(false);
                    isSelected_methodname.remove(methods.get(i));

                } else {
                    isSelected.put(i, true);
                    finalCheckBox.setChecked(true);
                    isSelected_methodname.add(methods.get(i));

                }


            }
        });


        methodView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("shotString", shotfile.getAbsolutePath()).putExtra("testcase", methods.get(i)).putStringArrayListExtra("caselist", methods);
                intent.setClass(context.getApplicationContext(), GalleryActivity.class);
                context.startActivity(intent);

                return false;
            }
        });


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected.get(i)) {
                    isSelected.put(i, false);
                    isSelected_methodname.remove(methods.get(i));

                } else {
                    isSelected.put(i, true);
                    isSelected_methodname.add(methods.get(i));

                }

            }

        });

        checkBox.setChecked(getIsSelected().get(i));
        methodView.setText(methods.get(i));

        return view;
    }


    public final class ViewCache{
        public TextView methodView, index;
        public CheckBox checkBox;
        public ImageView reviewPic, state;
        public LinearLayout review;
    }
}
