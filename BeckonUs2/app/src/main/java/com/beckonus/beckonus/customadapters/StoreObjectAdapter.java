package com.beckonus.beckonus.customadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beckonus.beckonus.R;
import com.beckonus.beckonus.customobjects.StoreObject;
import com.estimote.sdk.Beacon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shal on 10/04/2017.
 */

public class StoreObjectAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<StoreObject> storeList;

    public StoreObjectAdapter(Activity activity, ArrayList<StoreObject> list) {
        this.activity = activity;
        this.storeList = list;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fav_feed_item, null);
        }

        TextView uuid = (TextView) convertView.findViewById(R.id.storeName);
        TextView major = (TextView) convertView.findViewById(R.id.promo);

        StoreObject b = this.storeList.get(position);
        uuid.setText(b.name);
        major.setText(b.promotion.getTitle());
//        minor.setText(String.valueOf(b.getMinor()));
        return convertView;
    }
}
