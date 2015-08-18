package com.navtureapps.proximiioandroidtestapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings {
    private static final String TAG = "Settings";
    private static String appID;
    private static String authToken;
    private static final String defaultAppID = "-JsznWmFZ1Jj3pMU5Ann";
    private static final String defaultAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjQxMDI0NDQ3OTksInYiOjAsImQiOnsidWlkIjoiLUpzeXZEUXM2dVdUZVoyOHlySjkifSwiaWF0IjoxNDM5OTA5MDEyfQ.IK85WLe8zpQyQf_9omaN02UyVdogEd1jrSFJuLXN2es";

    public static String getAppID(Activity activity) {
        if (Settings.appID == null) {
            SharedPreferences preferences = activity.getSharedPreferences("ProximiioTestApp", Context.MODE_PRIVATE);
            String appID = preferences.getString("AppID", null);
            if (appID == null) {
                appID = defaultAppID;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("AppID", appID);
                editor.apply();
            }
            Settings.appID = appID;
        }
        Log.d(TAG, "Settings.appID: " + Settings.appID);
        return Settings.appID;
    }

    public static String getAuthToken(Activity activity) {
        if (Settings.authToken == null) {
            SharedPreferences preferences = activity.getSharedPreferences("ProximiioTestApp", Context.MODE_PRIVATE);
            String authToken = preferences.getString("AuthToken", null);
            if (authToken == null) {
                authToken = defaultAuthToken;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("AuthToken", authToken);
                editor.apply();
            }
            Settings.authToken = authToken;
        }
        Log.d(TAG, "Settings.authToken: " + Settings.authToken);
        return Settings.authToken;
    }

    public static void setAppID(Activity activity, String appID) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("ProximiioTestApp", Context.MODE_PRIVATE).edit();
        editor.putString("AppID", appID);
        editor.apply();
        Settings.appID = appID;
    }

    public static void setAuthToken(Activity activity, String authToken) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("ProximiioTestApp", Context.MODE_PRIVATE).edit();
        editor.putString("AuthToken", authToken);
        editor.apply();
        Settings.authToken = authToken;
    }
}
