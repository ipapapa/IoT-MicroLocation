package com.example.ma.contextualawarenessapplication;

/**
 * Created by Ma on 2016/3/20.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class BackgroundService extends Service implements BeaconConsumer {

    private static final String TAG = "com.Xinghua";
    public static final String ACTION_BROADCAST = BackgroundService.class.getName() + "Broadcast",
            LOG_MESSAGE = "LogMessage";
    private static int CountNumber = 0;
    private static String myLog;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;
    private BeaconManager beaconManager;


    private int myBeaconState = 0;

    public BackgroundService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand method called");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        // create a new thread run at the background of the device
        Runnable r = new Runnable() {
            @Override
            public void run() {

                /*
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
                                sendNotification("Notification: " + CountNumber);

                            }catch (Exception e){}
                        }
                    }
                }
                */
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
        beaconManager.unbind(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }



    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    MyBeacon myBeacon = new MyBeacon(beacon.toString());

                    /*
                    dbHandler.addBeacon(myBeacon);
                    try {
                        printDatabase();
                    }catch (Exception e){
                        Log.i("exxxx", e.toString());
                    }
                    */

                    if(beacon.getDistance() < 1)
                    {
                        if(beacon.toString().contains("11111111"))
                        {
                            if(myBeaconState != 1)
                            {
                                myBeaconState = 1;
                                sendNotification("Hello! The beacon is by your hand.");
                            }
                        }
                    }
                    else if(beacon.getDistance() < 5)
                    {
                        if(beacon.toString().contains("11111111"))
                        {
                            if(myBeaconState != 5)
                            {
                                myBeaconState = 5;
                                sendNotification("The beacon is 5 meters away from you now");
                            }
                        }
                    }
                    else if(beacon.getDistance() < 10)
                    {
                        if(beacon.toString().contains("11111111"))
                        {
                            if(myBeaconState != 10)
                            {
                                myBeaconState = 10;
                                sendNotification("The beacon is 10 meters away from you now");
                            }
                        }
                    }
                    sendBroadcastMessage(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    // send the information got from the background to the main activity
    private void sendBroadcastMessage(String message) {
        if (message != null) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(LOG_MESSAGE, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    // send the notification
    private void sendNotification(String notify) {
        // build the notification
        notification = new NotificationCompat.Builder(this);
        // cancel the notification when user respond
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        //============================= Send the notification ==============================================================
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Here is the title");
        notification.setContentText(notify);
        notification.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);
        //Intent intent = new Intent(BackgroundService.this, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //notification.setContentIntent(pendingIntent);

        // Build notification and issues it
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }

    private static String getCurrentTimeStamp() {
        Locale locale = new Locale("es", "ES");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", locale);
        Date now = new Date();
        return sdf.format(now);
    }

    /*
    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView editText = (TextView)GeofencingActivity.this
                        .findViewById(R.id.geofencingText);
                editText.append(line+"\n");
            }
        });
    }

    public void printDatabase() {
        TextView editText = (TextView)GeofencingActivity.this
                .findViewById(R.id.geofencingText);
        String dbString = dbHandler.databaseToString();
        editText.setText(dbString);
    }
    */
}