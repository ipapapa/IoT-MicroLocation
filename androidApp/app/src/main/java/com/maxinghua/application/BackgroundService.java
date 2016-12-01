package com.maxinghua.application;

/**
 * Created by MAXINGHUA on 2016/8/25.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.maxinghua.main.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RangedBeacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class BackgroundService extends Service implements BeaconConsumer{

    private static final String TAG = "com.Xinghua";

    private static final String ACTION_BROADCAST = BackgroundService.class.getName() + "Broadcast";
    private static final String LOG_MESSAGE = "LogMessage";

    private static final String COORDINATE_MESSAGE = "CoordinateMessage";
    private static final String DISTANCE_MESSAGE = "DistanceMessage";

    private static int CountNumber = 0;
    private static String myLog;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;
    private BeaconManager beaconManager;
    double d1 = 0, d2 = 0, d3 = 0;

    private int myBeaconState = 0;

    private URL url;
    private URLConnection connection;

    private DBHandler myDBHandler;

    public BackgroundService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand method called");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        RangedBeacon.setSampleExpirationMilliseconds(10000);
        //ArmaRssiFilter.setDEFAULT_ARMA_SPEED(1);

        myDBHandler = new DBHandler(this, null, null, 1);

        // restart the service if the service is closed somehow
        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy method called");
        beaconManager.unbind(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    String stringValue = "";
    private boolean unlock = true;

    @SuppressWarnings("deprecation")
    @Override
    public void onBeaconServiceConnect() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                beaconManager.setRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                        final Collection<Beacon> rangeBeacons = beacons;


                        for (Beacon beacon : rangeBeacons) {
                            if (beacon.toString().contains("25494")) {
                                d1 = beacon.getDistance();
                                if (d1 < 0.1 && myBeaconState != 1) {
                                    sendNotification("Microlocation Beacon: Pink", Color.MAGENTA);
                                    myBeaconState = 1;
                                    beacon.setRssi(-50);
                                }
                            } else if (beacon.toString().contains("57501")) {
                                d2 = beacon.getDistance();
                                if (d2 < 0.1 && myBeaconState != 2) {
                                    sendNotification("Microlocation Beacon: Yellow", Color.YELLOW);
                                    myBeaconState = 2;
                                    beacon.setRssi(-50);
                                }
                            } else if (beacon.toString().contains("30854")) {
                                d3 = beacon.getDistance();
                                if (d3 < 0.1 && myBeaconState != 3) {
                                    sendNotification("Microlocation Beacon: Purple", Color.RED);
                                    myBeaconState = 3;
                                    beacon.setRssi(-50);
                                }
                            } else {
                                // don't recognize this beacon
                                break;
                            }
                            try {
                                url = new URL("http://52.24.73.201/CASServer/MobileListener");
                                //url = new URL("http://10.0.2.2:8080/CASServer/MobileListener");
                                connection = url.openConnection();
                                connection.setDoOutput(true);
                                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                                out.write(d1 + "/" + d2 + "/" + d3);
                                out.close();

                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                String returnString = "";
                                while ((returnString = in.readLine()) != null) {
                                    stringValue = returnString;
                                }
                                in.close();
                                Log.i(TAG, stringValue);

                                String[] parseString = stringValue.split("/");


                                // Broadcast Message format: Coordinate X/Coordinate Y/distance 1/distance 2/distance 3
                                sendDistances(d1 + "/" + d2 + "/" + d3);

                                //sendNotification(parseString[1]);

                                if (!stringValue.contains("none")) {
                                    sendCoordinate(stringValue);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            boolean exist = myDBHandler.beaconExist(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString());
                            if (exist) {
                                myDBHandler.updateRecord(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), beacon.getDistance(), beacon.getRssi());
                            } else {
                                myDBHandler.addRecord(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), beacon.getDistance(), beacon.getRssi());
                            }
                        }
                    }

                    //Log.i(TAG, d1 + "/" + d2 + "/" + d3);
                    // send the broadcast: Coordinate X/Coordiante Y/Distance1/Distance2/Distance3

                    //sendBroadcastMessage(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                    // Set the beacon information as the application log
                    //((App) getApplicationContext()).setLog(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away."


                });
            }
        };
        Thread myThread = new Thread(r);
        myThread.start();


        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    private void sendCoordinate(String coordinate) {
        if (coordinate != null) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(COORDINATE_MESSAGE, coordinate);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Log.i("SEND_BROADCAST", coordinate);
        }
        //sendBroadcastMessage("Coordinate/" + coordinate);
    }

    // send Distances between 3 beacons
    private void sendDistances(String distances) {
        if (distances != null) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(DISTANCE_MESSAGE, distances);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Log.i("SEND_BROADCAST", distances);
        }
        //sendBroadcastMessage("Coordinate/" + coordinate);
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
    public void sendNotification(String notify, int color) {
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
        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);
        notification.setColor(color);

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

    public static String getActionBroadcast() {
        return ACTION_BROADCAST;
    }


    public static String getLogMessage() {
        return LOG_MESSAGE;
    }

    public static String getCoordinateMessage() { return COORDINATE_MESSAGE; }

    public static String getDistanceMessage() { return DISTANCE_MESSAGE; }

}