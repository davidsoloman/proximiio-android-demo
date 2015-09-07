package com.navtureapps.proximiioandroidtestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.navtureapps.proximiioandroid.Proximiio;
import com.navtureapps.proximiioandroid.ProximiioInput;
import com.navtureapps.proximiioandroid.ProximiioListener;

public class ScanActivity extends Activity {
    private static final String TAG = "ScanActivity";

    private String tag;
    private Switch iBeaconsEnabled;
    private Switch eddystoneEnabled;
    private Switch indoorAtlasEnabled;
    private Switch gpsGeofencesEnabled;
    private Switch steerPathEnabled;
    private TextView iBeaconPosition;
    private TextView eddystonePosition;

    private TextView indoorAtlasPosition;
    private TextView gpsGeofencePosition;
    private TextView steerPathPosition;
    private Proximiio proximiio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.main_bar, null);
            actionBar.setCustomView(cView);
        }

        iBeaconsEnabled = (Switch)findViewById(R.id.ibeaconswitch);
        eddystoneEnabled = (Switch)findViewById(R.id.eddystoneswitch);
        indoorAtlasEnabled = (Switch)findViewById(R.id.indooratlasswitch);
        gpsGeofencesEnabled = (Switch)findViewById(R.id.gpsgeofenceswitch);
        steerPathEnabled = (Switch)findViewById(R.id.steerpathswitch);
        iBeaconPosition = (TextView)findViewById(R.id.ibeaconposition);
        eddystonePosition = (TextView)findViewById(R.id.eddystoneposition);
        indoorAtlasPosition = (TextView)findViewById(R.id.indooratlasposition);
        gpsGeofencePosition = (TextView)findViewById(R.id.gpsgeofenceposition);
        steerPathPosition = (TextView)findViewById(R.id.steerpathposition);
        tag = "ScanActivity";
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Activity activity = this;

        ProximiioListener listener = new ProximiioListener() {
            @Override
            public void eventEnter(ProximiioInput input) {
                input.handleOutput(activity, null, true, false);
                Log.d(tag, "Event triggered when entered!");
            }

            @Override
            public void eventLeave(ProximiioInput input) {
                input.handleOutput(activity, null, true, false);
                Log.d(tag, "Event triggered when left!");
            }

            @Override
            public void error(final Error error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void scanChange(ProximiioInput.InputType type, boolean active) {
                updateScan(type, active);
            }

            @Override
            public void positionChange(double lat, double lon, double accuracy, ProximiioInput.InputType inputType) {
                updatePosition(lat, lon, accuracy, inputType);
            }
        };

        proximiio = new Proximiio(this, Settings.getAppID(this), Settings.getAuthToken(this), listener);
    }

    private void updatePosition(double lat, double lon, double accuracy, ProximiioInput.InputType inputType) {
        String text = "Latitude: " + String.valueOf(lat) + "\nLongitude: " + String.valueOf(lon) + "\nAccuracy: " + String.valueOf(accuracy);
        switch (inputType) {
            case iBeacon:
                setText(iBeaconPosition, text);
                break;
            case IndoorAtlas:
                setText(indoorAtlasPosition, text);
                break;
            case GPSGeofence:
                setText(gpsGeofencePosition, text);
                break;
            case SteerPath:
                setText(steerPathPosition, text);
                break;
            case EddyStone:
                setText(eddystonePosition, text);
                break;
        }
    }

    private void updateScan(ProximiioInput.InputType type, boolean active) {
        Log.d(TAG, "updating scan for: " + type + " (active)");
        switch (type) {
            case iBeacon:
                iBeaconsEnabled.setChecked(active);
                if (!active) {
                    setText(iBeaconPosition, "Disabled");
                }
                else {
                    setText(iBeaconPosition, "None");
                }
                break;
            case IndoorAtlas:
                indoorAtlasEnabled.setChecked(active);
                if (!active) {
                    setText(indoorAtlasPosition, "Disabled");
                }
                else {
                    setText(indoorAtlasPosition, "Move");
                }
                break;
            case GPSGeofence:
                gpsGeofencesEnabled.setChecked(active);
                if (!active) {
                    setText(gpsGeofencePosition, "Disabled");
                }
                else {
                    setText(gpsGeofencePosition, "None");
                }
                break;
            case SteerPath:
                steerPathEnabled.setChecked(active);
                if (!active) {
                    setText(steerPathPosition, "Disabled");
                }
                else {
                    setText(steerPathPosition, "None");
                }
                break;
            case EddyStone:
                eddystoneEnabled.setChecked(active);
                if (!active) {
                    setText(eddystonePosition, "Disabled");
                }
                else {
                    setText(eddystonePosition, "None");
                }
                break;
        }
    }

    private void setText(final TextView view, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proximiio.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        proximiio.stopActivity();
    }
}
