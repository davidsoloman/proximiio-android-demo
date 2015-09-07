package com.navtureapps.proximiioandroidtestapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings {
    private static String appID;
    private static String authToken;
    private static final String defaultAppID = "-JyOnqJ3Mvsw25D_MEHW";
    private static final String defaultAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjMxMjIwNjQwMDAsInYiOjAsImQiOnsidWlkIjoiLUp5T25PRXBvdlEtTGpBYmxtMUMifSwiaWF0IjoxNDQxMzk1MDI2fQ.qptcNnz1othZMpCnlforZ_fm6Ck9HbHIjohz55MBeks";
    
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
        return Settings.authToken;
    }

    public static void setAppID(Activity activity, String appID) {
        String originalAppId = getAppID(activity);
        SharedPreferences.Editor editor = activity.getSharedPreferences("ProximiioTestApp", Context.MODE_PRIVATE).edit();
        if (originalAppId != null && !originalAppId.equals(appID)) {
            Log.d("Settings", "API key has changed");
            SharedPreferences.Editor idEditor = activity.getSharedPreferences("ProximiioID", Context.MODE_PRIVATE).edit();
            idEditor.remove("ID");
        }
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
