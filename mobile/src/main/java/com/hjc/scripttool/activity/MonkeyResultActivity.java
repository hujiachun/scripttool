package com.hjc.scripttool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hjc.scripttool.R;

/**
 * Created by hujiachun on 15/12/15.
 */
public class MonkeyResultActivity extends Activity{
    TextView anr, fatal, crash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monkeyresult);
        anr = (TextView) this.findViewById(R.id.anr);
        crash = (TextView) this.findViewById(R.id.crash);
        fatal = (TextView) this.findViewById(R.id.fatal);

        Intent intent = getIntent();
        anr.setText("(" + intent.getIntExtra("anr", 0) + ")");
        crash.setText("(" + intent.getIntExtra("crash", 0) + ")");
        fatal.setText("(" + intent.getIntExtra("fatal", 0) + ")");


    }
}
