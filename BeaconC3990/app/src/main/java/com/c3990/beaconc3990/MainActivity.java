package com.c3990.beaconc3990;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] {notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EstimoteSDK.initialize(getApplicationContext(), "testing-dt3", "b102220219e238615b5a45e0694f7ddb");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
        EstimoteSDK.enableDebugLogging(true);

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d("Region", region.toString());

                // list has all of the beacons.
                // @TODO
                // iterate through the beacon data everytime and determine if there is a new beacon
                // that the client has found. If there is a new beacon issue the notification
                // and don't notify the user of that beacon until either the user leaves and reenters
                // or until a predetermined time has elapsed.
                Log.d("Beacon-Data", list.get(0).toString());

                // showNotification() builds the notification and sends it to the user task bar.
                // right now the notification does nothing but bring you back to the home page of
                // the app.
                // @TODO
                // write code to allow users, when clicked the notification is brought to a custom
                // intent that displays (for debugging purposes) the raw beacon data.
                showNotification("Beacon Detected", "You've Entered a Region where A Beacon Exists.");
            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                showNotification("Beacon Not Found", "You Have Left a Region where A Beacon Exsits.");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("nearby", null, null, null));
            }
        });

    }
}
