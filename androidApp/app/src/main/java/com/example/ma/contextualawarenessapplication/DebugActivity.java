package com.example.ma.contextualawarenessapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Ma on 2016/3/21.
 */
public class DebugActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //===================================================
        //setContentView(R.layout.detail_log);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String logMessage = intent.getStringExtra(BackgroundService.LOG_MESSAGE);
                        //ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                        //LayoutInflater inflater = getLayoutInflater();
                        //TextView textView = (TextView) inflater.inflate(R.layout.detail_log, viewGroup, false);
                        TextView textView = (TextView)findViewById(R.id.logMessage);
                        if(textView != null) textView.append(logMessage+"\n");
                    }
                }, new IntentFilter(BackgroundService.ACTION_BROADCAST)
        );


    }
}
