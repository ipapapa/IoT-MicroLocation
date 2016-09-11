package com.maxinghua.viewpager;

/**
 * Created by Ma on 2016/8/10.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import com.maxinghua.main.MainActivity;


public class WelcomeAct extends Activity {

    private boolean isFirstIn = false;
    private static final int TIME = 2000;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;


    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;

                case GO_GUIDE:
                    goGuide();
                    break;
            }

        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.welcome);
        init();
    }

    private void init(){
        SharedPreferences perPreferences = getSharedPreferences("xinghua", MODE_PRIVATE);
        isFirstIn = perPreferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
//            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
//            mHandler.sendEmptyMessage(GO_HOME);
            mHandler.sendEmptyMessage(GO_HOME);
        }else{
//            mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
            mHandler.sendEmptyMessage(GO_GUIDE);
            Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();
        }

    }

    private void goHome(){
        Intent i = new Intent(WelcomeAct.this,MainActivity.class);
        startActivity(i);
        finish();
    }
    private void goGuide(){
        Intent i = new Intent(WelcomeAct.this,Guide.class);
        startActivity(i);
        finish();
    }

}
