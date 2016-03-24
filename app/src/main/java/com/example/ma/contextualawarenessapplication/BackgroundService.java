package com.example.ma.contextualawarenessapplication;

/**
 * Created by Ma on 2016/3/20.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BackgroundService extends Service {

    private static final String TAG = "com.Xinghua";
    public static final String ACTION_BROADCAST = BackgroundService.class.getName() + "Broadcast",
            LOG_MESSAGE = "LogMessage";
    private static int CountNumber = 0;
    private static String myLog;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;


    public BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand method called");

        // build the notification
        notification = new NotificationCompat.Builder(this);
        // cancel the notification when user respond
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);

        // create a new thread run at the background of the device
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for ( int i = 0; i < 100; i++){
                    long futureTime = System.currentTimeMillis() + 5000;
                    while (System.currentTimeMillis() < futureTime) {
                        synchronized (this) {
                            try{
                                wait(futureTime - System.currentTimeMillis());
                                Log.i(TAG, "service is doing something");
                                CountNumber++;
                                myLog = "service is doing something: " + CountNumber;
                                sendBroadcastMessage(myLog);

                                //============================= Send the notification ==============================================================
                                notification.setTicker("This is the ticker");
                                notification.setWhen(System.currentTimeMillis());
                                notification.setContentTitle("Here is the title");
                                notification.setContentText("Notification" + CountNumber);
                                Intent intent = new Intent(BackgroundService.this, MainActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                notification.setContentIntent(pendingIntent);

                                // Build notification and issues it
                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                nm.notify(uniqueID, notification.build());
                            }catch (Exception e){}
                        }
                    }
                }
            }
        };

        // run the thread
        Thread myThread = new Thread(r);
        myThread.start();
        // restart the service if the service is closed somehow
        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy method called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    // send the information gor from the background to the main activity
    private void sendBroadcastMessage(String string) {
        if (string != null) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(LOG_MESSAGE, string);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}