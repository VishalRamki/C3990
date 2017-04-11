package com.beckonus.beckonus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.beckonus.beckonus.customadapters.BeaconListAdapter;
import com.beckonus.beckonus.customadapters.PromotionObjectAdapter;
import com.beckonus.beckonus.customobjects.LivingDataStore;
import com.beckonus.beckonus.customobjects.PromotionObject;
import com.beckonus.beckonus.customobjects.User;
import com.beckonus.beckonus.rest.RestClient;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.GsonBuilder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DefaultFeedActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private ArrayList<Beacon> passedBeacons;
    private ArrayList<PromotionObject> passedPromotions;
    private ListView listview;
    private BeaconListAdapter bla;
    private PromotionObjectAdapter poa;
    private AppCompatActivity aca;
    private String beaconCurrent;

    public void showNotification(String uuid, String title, String message) {
        Intent notifyIntent = new Intent(this, PromotionActivity.class);
        notifyIntent.putExtra("uuid", uuid);
        notifyIntent.putExtra("iStore", "no");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        popup.inflate(R.menu.game_menu);
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.viewLocation:
                Intent i = new Intent(aca, CurrentLocationActivity.class);
                startActivity(i);
                return true;
            case R.id.viewFavStores:
                Intent x = new Intent(aca, FavouriteStores.class);
                startActivity(x);
                return true;
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_feed);
        aca = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        passedBeacons = new ArrayList<Beacon>();
        listview = (ListView) findViewById(R.id.nearbyfeed);
        bla = new BeaconListAdapter(this, passedBeacons);
//        listview.setAdapter(bla);

        passedPromotions = new ArrayList<PromotionObject>();
        poa = new PromotionObjectAdapter(this, passedPromotions);
        listview.setAdapter(poa);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent notifyIntent = new Intent(aca, PromotionActivity.class);
                notifyIntent.putExtra("uuid", passedPromotions.get(position).getPromotion_id());
                notifyIntent.putExtra("iStore", "no");
                startActivity(notifyIntent);
            }
        });

        EstimoteSDK.initialize(getApplicationContext(), "testing-dt3", "b102220219e238615b5a45e0694f7ddb");

        // uncomment to enable debug-level logging
        // it's usually only a good idea when troubleshooting issues with the Estimote SDK
        EstimoteSDK.enableDebugLogging(true);

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

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

                System.out.println(list.toString());
                for (int i = 0; i < list.size(); i++) {
                    Log.d("Beacon-Data", list.get(i).toString());
                    Log.d("Beacon-Data", list.get(i).getMacAddress().toString());
                    Log.d("Beacon-Data", list.get(i).getProximityUUID().toString());
                    Log.d("Beacon-Data", String.valueOf(list.get(i).getMajor()));
                    Log.d("Beacon-Data", String.valueOf(list.get(i).getMinor()));


                    passedBeacons.add(list.get(i));
//                    System.out.println(passedBeacons.toString());
                }

                JSONObject j = new JSONObject();
                try {
                    j.put("beacon_id", list.get(0).getProximityUUID().toString());
                    beaconCurrent = list.get(0).getProximityUUID().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RestClient.get(aca, "/api/beacon/store", j, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                        System.out.println("JSOBOB");
                        System.out.println(timeline.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        System.out.println("JSONARRAY");
                        System.out.println(timeline);
                        try {
                            // we have the data from the server. It should only return a single document
                            // delimited by 0
                            JSONObject document = timeline.getJSONObject(0);

                            // convert it to objects;
                            JSONArray promotion = document.getJSONArray("promotion");
                            System.out.println(promotion);

                            com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson gso = new GsonBuilder().create();
                            PromotionObject po = gso.fromJson(String.valueOf(promotion.get(0)), PromotionObject.class);
                            System.out.println(po.toString());

                            po.setStoreName(document.getString("name"));
                            po.inClientDateTime = DateFormat.getDateTimeInstance().format(new Date());

                            if (document.has("long") && document.has("lat")) {
                                po.setLonLat(document.getString("long"), document.getString("lat"));
                                Toast.makeText(DefaultFeedActivity.this, document.getString("long"), Toast.LENGTH_SHORT).show();
                            }
                            // add the sroe;
                            LivingDataStore.addLivingPromotion(po);
                            passedPromotions.add(po);
                            poa.notifyDataSetChanged();


                            // showNotification() builds the notification and sends it to the user task bar.
                            // right now the notification does nothing but bring you back to the home page of
                            // the app.
                            // @TODO
                            // write code to allow users, when clicked the notification is brought to a custom
                            // intent that displays (for debugging purposes) the raw beacon data.
                            showNotification(po.getPromotion_id(), po.getTitle(), po.getMessage());

                            // Now tell the server, hey we interacted with this beacon;
                            JSONObject interactObject = new JSONObject();
                            interactObject.put("user_id", "testing");
                            JSONObject interObj = new JSONObject();
                            interObj.put("store_id", document.get("store_id"));
                            interObj.put("beacon_id", beaconCurrent);
                            interObj.put("promotion_id", po.getPromotion_id());
                            interactObject.put("update", interObj.toString());
                            System.out.println(interactObject.toString());
                            RestClient.post(aca, "/api/user/interact/beacons", interactObject, new JsonHttpResponseHandler() {
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject j) {
                        System.out.println(statusCode);
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                        System.out.println(statusCode);
                        System.out.println(e.toString());
                        System.out.println(response);
                    }
                });

                bla.notifyDataSetChanged();
            }

            @Override
            public void onExitedRegion(Region region) {

            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d("BeaconManager", "F");
                beaconManager.startMonitoring(new Region("nearby", null, null, null));
            }
        });

    }

}
