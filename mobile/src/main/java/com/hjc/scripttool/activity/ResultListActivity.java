package com.hjc.scripttool.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hjc.scripttool.R;
import com.hjc.scripttool.view.ResultInfoView;
import com.hjc.scriptutil.MRInfo;
import com.hjc.util.Constants;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by hujiachun on 15/12/14.
 */
public class ResultListActivity extends Activity{
    public ListView listView;
    public ArrayList<MRInfo> resultInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monkeyresultlist);
        resultInfos = new ArrayList<>();
        File file = new File(Constants.MONKEY_PATH);
        String[] historyList = file.list();
        if(historyList != null){
            for(String history : historyList){
                MRInfo mrInfo = new MRInfo();
                mrInfo.setHistory(history);
                mrInfo.setAnr(0);
                mrInfo.setCrash(0);
                mrInfo.setFatal(0);
                mrInfo.setCheck("check");
                resultInfos.add(mrInfo);
            }
            listView = (ListView) this.findViewById(R.id.listView);

            ResultInfoView adapter = new ResultInfoView(this, resultInfos, R.layout.monkeyitem);
            listView.setAdapter(adapter);
        }

        else{
            Toast.makeText(getApplicationContext(), "not find result", Toast.LENGTH_SHORT).show();
        }

    }
}
