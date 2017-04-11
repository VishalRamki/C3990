package com.beckonus.beckonus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.beckonus.beckonus.customadapters.BeaconListAdapter;
import com.beckonus.beckonus.customadapters.StoreObjectAdapter;
import com.beckonus.beckonus.customobjects.LivingDataStore;
import com.beckonus.beckonus.customobjects.PromotionObject;
import com.beckonus.beckonus.customobjects.StoreObject;
import com.beckonus.beckonus.customobjects.User;
import com.beckonus.beckonus.rest.RestClient;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FavouriteStores extends AppCompatActivity {

    private AppCompatActivity aca;
    private ArrayList<StoreObject> stores;
    private ListView listview;
    private StoreObjectAdapter soa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_stores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aca = this;
        stores = new ArrayList<StoreObject>();
        listview = (ListView) findViewById(R.id.favStores);
        soa = new StoreObjectAdapter(this, stores);
        listview.setAdapter(soa);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(stores.get(position).toString());
                Intent i = new Intent(aca, PromotionActivity.class);
                i.putExtra("iStore", stores.get(position).store_id);
                startActivity(i);
            }
        });

        JSONObject jO = new JSONObject();
        try {
            jO.put("user_id", User.ID);
            RestClient.get(this, "/api/user/favourite", jO, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {
                    System.out.println("JSOBOB");
                    System.out.println(timeline.toString());
                    try {
                        JSONArray favouriteStores = timeline.getJSONArray("favouritestores");
                        int i = 0;
                        for (i = 0; i < favouriteStores.length(); i++) {
                            JSONObject jo = (JSONObject) favouriteStores.get(i);
                            com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson gso = new GsonBuilder().create();
                            StoreObject po = gso.fromJson(String.valueOf(jo), StoreObject.class);
                            po.longti = jo.getString("long");
                            LivingDataStore.favStores.put(jo.getString("store_id"), po);
                        }
                        stores.addAll(LivingDataStore.favStores.values());
                        soa.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    System.out.println("JSONARRAY");
                    System.out.println(timeline);
                    try {
                        JSONObject document = (JSONObject) timeline.get(0);
                        // now pull out stores
                        JSONArray storeArray = document.getJSONArray("favouritestores");
                        com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson gso = new GsonBuilder().create();
                        StoreObject po = gso.fromJson(String.valueOf(storeArray.getJSONObject(0)), StoreObject.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
