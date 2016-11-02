package com.maxinghua.application;

import android.app.Application;
import android.content.Intent;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


/**
 * Created by admin on 2016/8/9.
 */
public class App extends Application{

    private String log = "";
    private static boolean BACKGROUND_ENABLED = false;



    private boolean login = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //Add Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //==============Start the background service===============================================
        if(!BACKGROUND_ENABLED) {
            BACKGROUND_ENABLED = true;

            Intent i = new Intent(this, BackgroundService.class);
            startService(i);
        }
    }


    //========================== Getters and Setters ========================================================================
    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
