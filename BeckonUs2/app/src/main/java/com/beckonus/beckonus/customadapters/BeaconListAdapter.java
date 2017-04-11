package com.beckonus.beckonus.customadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beckonus.beckonus.R;
import com.estimote.sdk.Beacon;

import java.util.List;

/**
 * Created by Shal on 22/03/2017.
 */

public class BeaconListAdapter extends BaseAdapter {
    private Activity activity;
    private List<Beacon> beaconList;
    private LayoutInflater inflater;

    public BeaconListAdapter(Activity activity, List<Beacon> beacons) {
        this.activity = activity;
        this.beaconList = beacons;
    }

    @Override
    public int getCount() {
        return this.beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.beaconList.get(position);
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
            convertView = inflater.inflate(R.layout.feed_item, null);
        }

        TextView uuid = (TextView) convertView.findViewById(R.id.uuid);
        TextView major = (TextView) convertView.findViewById(R.id.major);
        TextView minor = (TextView) convertView.findViewById(R.id.minor);

        Beacon b = this.beaconList.get(position);
        uuid.setText(b.getProximityUUID().toString());
        major.setText(String.valueOf(b.getMajor()));
        minor.setText(String.valueOf(b.getMinor()));
        return convertView;
    }
}
