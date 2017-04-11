package com.beckonus.beckonus.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by Shal on 03/04/2017.
 */

public class RestClient {

    public static void get(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        BaseClient.getWithJSON(c, url, jObj, responseHandler);
    }

    public static void post(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        BaseClient.postWithJSON(c, url, jObj, responseHandler);
    }

    public static void put(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        BaseClient.putWithJSON(c, url, jObj, responseHandler);
    }

    public static void delete(Context c, String url, JSONObject jObj, AsyncHttpResponseHandler responseHandler) {
        BaseClient.delWithJSON(c, url, jObj, responseHandler);
    }
}
