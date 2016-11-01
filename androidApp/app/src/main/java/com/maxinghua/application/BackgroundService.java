package com.maxinghua.application;

/**
 * Created by MAXINGHUA on 2016/8/25.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.maxinghua.main.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
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

//    public Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            sendBroadcastMessage(content);
//        }
//    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand method called");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        myDBHandler = new DBHandler(this, null, null, 1);
        // create a new thread run at the background of the device
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    socket = new Socket(HOST, PORT);
//                    in = new BufferedReader(new InputStreamReader(socket
//                            .getInputStream()));
//                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                            socket.getOutputStream())), true);
//
//                    while (true) {
//                        if (!socket.isClosed()) {
//                            if (socket.isConnected()) {
//                                if (!socket.isInputShutdown()) {
//                                    if ((content = in.readLine()) != null) {
//                                        content += "\n";
//                                        mHandler.sendMessage(mHandler.obtainMessage());
//                                    } else {
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                // keep update notifications in background
//                for ( int i = 0; i < 100; i++){
//                    long futureTime = System.currentTimeMillis() + 5000;
//                    while (System.currentTimeMillis() < futureTime) {
//                        synchronized (this) {
//                            try{
//                                wait(futureTime - System.currentTimeMillis());
//                                Log.i(TAG, "service is doing something");
//                                CountNumber++;
//                                myLog = "service is doing something: " + CountNumber;
//                                //sendNotification("Notification: " + CountNumber);
//                                //((App) getApplicationContext()).setLog("Debug Log: " + CountNumber);
//                                sendBroadcastMessage("Debug Log: " + CountNumber);
//
//                            }catch (Exception e){}
//                        }
//                    }
//                }
//
//            }
//        };
//
//        // run the thread
//        Thread myThread = new Thread(r);
//        myThread.start();


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

    @SuppressWarnings("deprecation")
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    if (beacon.toString().contains("57894")) {
                        d1 = beacon.getDistance();
                    } else if (beacon.toString().contains("41831")) {
                        d2 = beacon.getDistance();
                    } else if (beacon.toString().contains("23833")) {
                        d3 = beacon.getDistance();
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

                        String returnString="";
                        while ((returnString = in.readLine()) != null)
                        {
                            stringValue = returnString;
                        }
                        in.close();
                        Log.i(TAG, stringValue);

                        String[] parseString = stringValue.split("/");

                        sendNotification(parseString[1]);
                        sendCoordinate(stringValue);

                        //if(!stringValue.contains("none")){
                        //    sendCoordinate(stringValue);
                        //}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    boolean exist = myDBHandler.beaconExist(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString());
                    if(exist) {
                        myDBHandler.updateRecord(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), beacon.getDistance(), beacon.getRssi());
                    }
                    else {
                        myDBHandler.addRecord(beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString(), beacon.getDistance(), beacon.getRssi());
                    }
                    //Log.i(TAG, d1 + "/" + d2 + "/" + d3);
                    sendBroadcastMessage("Beacon received!");
                    //sendBroadcastMessage(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                    // Set the beacon information as the application log
                    //((App) getApplicationContext()).setLog(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                }
            }
        });

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
        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
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

    public static String getActionBroadcast() {
        return ACTION_BROADCAST;
    }


    public static String getLogMessage() {
        return LOG_MESSAGE;
    }

    public static String getCoordinateMessage() { return COORDINATE_MESSAGE; }

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