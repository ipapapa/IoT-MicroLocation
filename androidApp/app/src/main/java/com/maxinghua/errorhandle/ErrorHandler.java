package com.maxinghua.errorhandle;

/**
 * Created by Ma on 2016/8/11.
 */
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.lang.Thread.UncaughtExceptionHandler;


/**
 * UncaughtExceptionHandler
 */
public class ErrorHandler implements UncaughtExceptionHandler {

    /**
     * This code is used in case any uncaught exception happens
     */
    public void uncaughtException(Thread a, Throwable e) {
        System.out.println("This is:" + a.getName() + ",Message:" + e.getMessage());
        e.printStackTrace();

    }

}