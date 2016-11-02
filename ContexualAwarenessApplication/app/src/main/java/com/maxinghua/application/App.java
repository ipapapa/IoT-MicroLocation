package com.maxinghua.application;

import android.app.Application;
import android.content.Intent;


/**
 * Created by admin on 2016/8/9.
 */
public class App extends Application{

    private String log = "";
    private static boolean BACKGROUND_ENABLED = false;


    @Override
    public void onCreate() {
        super.onCreate();

        //==============Start the background service===============================================
        if(!BACKGROUND_ENABLED) {
            BACKGROUND_ENABLED = true;

            Intent i = new Intent(this, BackgroundService.class);
            startService(i);
        }

//        // create a loop to keep updating the logview
//        Thread thread=new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                int counter = 0;
//                while (true) {
//                    log = "Counting number: " + counter;
//                    counter++;
//
//                    try{
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
    }


    //========================== Getters and Setters ========================================================================
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
