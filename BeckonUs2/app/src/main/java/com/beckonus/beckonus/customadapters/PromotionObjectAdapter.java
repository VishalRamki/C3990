package com.beckonus.beckonus.customadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beckonus.beckonus.R;
import com.beckonus.beckonus.customobjects.DownloadImageTask;
import com.beckonus.beckonus.customobjects.PromotionObject;
import com.beckonus.beckonus.customobjects.StoreObject;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Shal on 10/04/2017.
 */

public class PromotionObjectAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<PromotionObject> promotionObjects;

    public PromotionObjectAdapter(Activity activity, ArrayList<PromotionObject> list) {
        this.activity = activity;
        this.promotionObjects = list;
    }

    @Override
    public int getCount() {
        return promotionObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return promotionObjects.get(position);
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
            convertView = inflater.inflate(R.layout.default_promo_feed, null);
        }

        TextView uuid = (TextView) convertView.findViewById(R.id.promoTitle);
        TextView major = (TextView) convertView.findViewById(R.id.store);
        TextView minor = (TextView) convertView.findViewById(R.id.date);

        ImageView iv2 = (ImageView) convertView.findViewById(R.id.imageView2);

        PromotionObject b = this.promotionObjects.get(position);
        uuid.setText(b.getTitle());
        major.setText(b.getStoreName());
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageView2)).execute(b.getImageLocation());;
        minor.setText("Date/Time Passed: " + b.inClientDateTime);
        return convertView;
    }
}
