package com.a812003067.beaconbuild2;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Shal on 01/04/2017.
 */

public class BeckonUsServerRESTClient {
    private static final String BASE_URL = "http://45.63.106.236:5000";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getWithJSON(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(jObj.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.get(c, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    public static void postWithJSON(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(jObj.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(c, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    public static void get(Context c, String url, StringEntity ent, String app, AsyncHttpResponseHandler responseHandler) {
        client.get(c, getAbsoluteUrl(url), ent, app, responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
//        System.out.println(getAbsoluteUrl(url));
//        client.get("http://45.63.106.236:5000/api/user/", params, responseHandler);

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
