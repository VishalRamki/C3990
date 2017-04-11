package com.beckonus.beckonus;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.renderscript.ScriptGroup;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beckonus.beckonus.customobjects.DownloadImageTask;
import com.beckonus.beckonus.customobjects.LivingDataStore;
import com.beckonus.beckonus.customobjects.PromotionObject;
import com.beckonus.beckonus.customobjects.User;
import com.beckonus.beckonus.mapsystem.CustomLocationService;
import com.beckonus.beckonus.rest.RestClient;
import com.beckonus.beckonus.rest.networking;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class PromotionActivity extends AppCompatActivity {

    private AppCompatActivity aca;
    private PromotionObject promo;
    private boolean isStore = false;
    private String store_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomLocationService cls = new CustomLocationService(this);

        aca = this;
        String UUID = "";

        // this is to get hte passed in UUID;
        Bundle extras = getIntent().getExtras();
        isStore = false;
        store_id = "";
        if (extras != null) {
            UUID = extras.getString("uuid");
            if (extras.getString("iStore").equals("no")) {
                isStore = false;
            } else {
                isStore = true;
                store_id = extras.getString("iStore");
            }
        }

        System.out.println("This is a Promotion Intent.");
//        System.out.println(LivingDataStore.getPromotionObjectByUUID(UUID));
        // get the data;
        if (isStore) {
            System.out.println(store_id);
            System.out.println(LivingDataStore.favStores.get(store_id));
            System.out.println(LivingDataStore.favStores.get(store_id));
            promo = LivingDataStore.favStores.get(store_id).promotion;
        } else {
            promo = LivingDataStore.getPromotionObjectByUUID(UUID);
        }

        TextView title = (TextView) findViewById(R.id.title);
        TextView message = (TextView) findViewById(R.id.message);
        TextView store = (TextView) findViewById(R.id.store_name);
        TextView expires = (TextView) findViewById(R.id.expires);

        title.setText(promo.getTitle());
        message.setText(promo.getMessage());
        expires.setText("Expires ON: " + promo.getExpires());
        store.setText(promo.getStoreName());
        System.out.println(promo.getImageLocation());
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(promo.getImageLocation());

        Button viewMap = (Button) findViewById(R.id.getDirections);
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localLat = "";
                String localLon = "";

                if (isStore) {
                    localLat = LivingDataStore.favStores.get(store_id).lat;
                    localLon = LivingDataStore.favStores.get(store_id).longti;
                } else {
                    localLon = promo.getLongtitude();
                    localLat = promo.getLatitude();
                }

                if (localLat.isEmpty() && localLon.isEmpty() ) {
                    Toast.makeText(aca, "The Store owner has not updated their Location.", Toast.LENGTH_SHORT).show();
                } else {
                    if (User.uLong.isEmpty() && User.uLat.isEmpty()) {
                        Toast.makeText(aca, "Your location services are disabled, please re-enable it and then attempt to get Directions.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(aca, DirectionMapActivity.class);
                        i.putExtra("long", localLon);
                        i.putExtra("lat", localLat);
                        i.putExtra("olat", User.uLat);
                        i.putExtra("olong", User.uLong);
                        startActivity(i);
                    }

                }

            }
        });

        ImageButton ib = (ImageButton) findViewById(R.id.favouriteStore);

        JSONObject j = new JSONObject();
        try {
            j.put("user_id", User.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestClient.get(aca, "/api/user/favourite", j, new JsonHttpResponseHandler() {
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
                    JSONObject document = timeline.getJSONObject(0);
                    System.out.println(document);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        ib.setImageDrawable(getApplicationContext().getDrawable(android.R.drawable.star_big_off));


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject j = new JSONObject();
                try {
                    j.put("user_id", User.ID);
                    j.put("store_id", promo.getStore_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RestClient.post(aca, "/api/user/favourite",j, new JsonHttpResponseHandler() {

                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
