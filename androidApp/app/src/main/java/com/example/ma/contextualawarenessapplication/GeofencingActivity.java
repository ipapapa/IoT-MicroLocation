package com.example.ma.contextualawarenessapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.RemoteException;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class GeofencingActivity extends AppCompatActivity implements BeaconConsumer {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    protected static final String TAG = "GeofencingActivity";
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    logToDisplay(getCurrentTimeStamp() + " | Beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                }
            /*if (beacons.size() > 0) {
            	EditText editText = (EditText)GeofencingActivity.this
						.findViewById(R.id.geofencingText);
                Beacon firstBeacon = beacons.iterator().next();
                logToDisplay("The first beacon "+firstBeacon.toString()+" is about "+firstBeacon.getDistance()+" meters away.");            }
        */}

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView editText = (TextView)GeofencingActivity.this
                        .findViewById(R.id.geofencingText);
                editText.append(line+"\n");
            }
        });
    }

    private static String getCurrentTimeStamp() {
        Locale locale = new Locale("es", "ES");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", locale);
        Date now = new Date();
        return sdf.format(now);
    }
}