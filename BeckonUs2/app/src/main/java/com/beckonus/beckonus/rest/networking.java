package com.beckonus.beckonus.rest;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Shal on 09/04/2017.
 */

public class networking {

    public static void LoadImageFromWebOperations(String url, ImageView iv) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            iv.setImageDrawable(d);
        } catch (Exception e) {
//            return null;
        }
    }


}
