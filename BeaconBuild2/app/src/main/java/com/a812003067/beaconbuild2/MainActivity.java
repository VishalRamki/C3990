package com.a812003067.beaconbuild2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.plus.model.people.Person;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gtNearBy = (Button) findViewById(R.id.goToBeacons);
        Button gtFeed = (Button) findViewById(R.id.goToFeed);
        Button gtStore = (Button) findViewById(R.id.goToStoreMan);
        Button gtMaps = (Button) findViewById(R.id.gtMaps);

        gtNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NearByBeacons.class);
                startActivity(i);
            }
        });

        gtMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
        }
    });

//        JSONServerInteraction jsi = new JSONServerInteraction();
//        String x = String.valueOf(jsi.execute("http://45.63.106.236:5000/api"));
//        JSONServerInteraction i = new JSONServerInteraction();
//        i.execute("http://45.63.106.236:5000/api");

        JSONObject userGet = new JSONObject();
        try {
            userGet.put("user_id", "1888fd78-cd43-40ac-82af-692b099c92a8");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BeckonUsServerRESTClient.getWithJSON(this, "/api/user/", userGet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                System.out.println(timeline.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject j) {
                System.out.println(statusCode);//
                System.out.println(j.toString());
            }
        });


    }
}
