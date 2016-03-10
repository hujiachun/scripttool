package com.hjc.scripttool.view;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjc.scripttool.R;
import com.hjc.scripttool.activity.ChartActivity;
import com.hjc.scripttool.activity.PerformanceCaseActitity;
import com.hjc.scripttool.activity.PerformanceHistoryAcitity;
import com.hjc.scriptutil.Performanceinfo;
import com.hjc.scriptutil.Settings;
import com.hjc.util.Constants;
import com.hjc.util.Util;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

/**
 * Created by hujiachun on 16/3/2.
 */
public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.ItemViewHolder> {
    public ArrayList<Performanceinfo> items;
    private OnRecyclerViewItemClickListener listener;
    public Context context;

    public PerformanceAdapter(ArrayList<Performanceinfo> items, Context context) {
        this.items = items;
        this.context = context;
    }


    //点击监听事件
    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    //设置监听器
    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //将布局进行绑定
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.performanceinfo, viewGroup, false);


        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {

        final Performanceinfo item = items.get(i);
        itemViewHolder.name.setText(item.getName());
        itemViewHolder.time.setText(item.getTime());
        itemViewHolder.review.setImageResource(item.getRereview_img());
        itemViewHolder.delete.setImageResource(item.getDelete_img());

        itemViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (this) {
                    int position = itemViewHolder.getLayoutPosition();
                    if(position != -1){
                        Log.e(Constants.TAG, i + ":" + position);
                        Util.deleteDir(new File(Constants.PERFORMANCE_PATH + items.get(position).getName() + "_" + items.get(position).getTime()));
                        items.remove(position);
                        notifyItemRemoved(position);
//                        notifyAll();
                    }
                }
            }
        });

        itemViewHolder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e(Constants.TAG, item.getName());
//                Intent intent = new Intent();
//                intent.putExtra(Constants.PERFORMANCE_DATA, item.getName()+ "_"+ item.getTime());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setClass(context.getApplicationContext(), ChartActivity.class);
//                context.startActivity(intent);

                Intent intent = new Intent();
                File file = new File(Constants.PERFORMANCE_PATH + itemViewHolder.name.getText() + "_" + itemViewHolder.time.getText());
                String[] list = file.list();


                List<String> hisList = new ArrayList<>(Arrays.asList(list));
                intent.putStringArrayListExtra(Constants.CASE_LIST, (ArrayList<String>) hisList).
                        putExtra(Constants.TYPE, itemViewHolder.name.getText()).putExtra(Constants.TIME, itemViewHolder.time.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context, PerformanceCaseActitity.class);
                context.startActivity(intent);
            }
        });

        if (listener != null) {
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                   int pos = itemViewHolder.getLayoutPosition();
                   listener.onItemClick(itemViewHolder.itemView, pos);
               }

       });

        itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = itemViewHolder.getLayoutPosition();
                listener.onItemLongClick(itemViewHolder.itemView, pos);
                return false;
            }
        });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolder，用于缓存，提高效率
    public final static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        ImageView delete;
        ImageView review;
        ImageView name_img;
        ImageView time_img;


        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.preformance_name);
            time = (TextView) itemView.findViewById(R.id.preformance_time);
            delete = (ImageView) itemView.findViewById(R.id.preformance_delete);
            review = (ImageView) itemView.findViewById(R.id.preformance_review);
            name_img = (ImageView) itemView.findViewById(R.id.name_img);
            time_img = (ImageView) itemView.findViewById(R.id.time_img);


        }
    }
}

