package com.beckonus.beckonus.customobjects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.beckonus.beckonus.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Shal on 09/04/2017.
 */

public class User {
    // should be initalized in the google login;
    public static String ID = "testing";
    public static String uLong = "";
    public static String uLat = "";
    public static GoogleApiClient mGoogleApiClient;
}

