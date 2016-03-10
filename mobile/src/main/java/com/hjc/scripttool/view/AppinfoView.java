package com.hjc.scripttool.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjc.scripttool.R;
import com.hjc.scriptutil.Appinfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hujiachun on 15/11/13.
 */
public class AppinfoView extends BaseAdapter {
    public static ArrayList<String> isSelected_appname = null;
    public static ArrayList<String> isSelected_package = null;
    public static HashMap<Integer, Boolean> isSelected;
    private LayoutInflater inflater;
    private ArrayList<Appinfo> appinfos;
    private int resource;

    public AppinfoView(Context context, ArrayList<Appinfo> appinfos, int resource) {
        this.appinfos = appinfos;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isSelected = new HashMap<Integer, Boolean>();
        isSelected_package = new ArrayList<>();
        isSelected_appname = new ArrayList<>();
        // 初始化数据
        initDate();
    }

    public HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public  ArrayList<String> getSelected_appname() {
        return isSelected_appname;
    }

    public  ArrayList<String> getSelected_packagename() {
        return isSelected_package;
    }

    @Override
    public int getCount() {
        return appinfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView packageView = null;
        CheckBox checkBox = null;
        TextView nameView = null;
        ImageView icon = null;
        if(convertView == null){
            convertView = inflater.inflate(resource, null);
            packageView = (TextView) convertView.findViewById(R.id.list);
            nameView = (TextView) convertView.findViewById(R.id.appname);
            checkBox= (CheckBox) convertView.findViewById(R.id.packagecheck);
            icon= (ImageView) convertView.findViewById(R.id.icon);
            ViewCache cache = new ViewCache();
            cache.packageView = packageView;
            cache.checkBox = checkBox;
            cache.nameView = nameView;
            cache.icon = icon;
            convertView.setTag(cache);
        }

        ViewCache cache = (ViewCache) convertView.getTag();
        packageView = cache.packageView;
        nameView = cache.nameView;
        checkBox = cache.checkBox;
        icon = cache.icon;
        packageView.setText( appinfos.get(position).getPackageName());
        nameView.setText(appinfos.get(position).getName());
        icon.setImageDrawable(appinfos.get(position).getIcon());


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    isSelected_package.remove(appinfos.get(position).getPackageName());
                    isSelected_appname.remove(appinfos.get(position).getName().toString());


                } else {
                    isSelected.put(position, true);
                    isSelected_package.add(appinfos.get(position).getPackageName());
                    isSelected_appname.add(appinfos.get(position).getName().toString());

                }



            }
        });
        checkBox.setChecked(getIsSelected().get(position));

        return convertView;
    }

    private void initDate() {
        for (int i = 0; i < appinfos.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        AppinfoView.isSelected = isSelected;
    }


    public  ArrayList<String> getSelected(){
        return isSelected_package;
    }
    private final class ViewCache{
        public TextView packageView;
        public TextView nameView;
        public CheckBox checkBox;
        public ImageView icon;

    }
}
