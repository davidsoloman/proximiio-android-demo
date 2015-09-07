package com.navtureapps.proximiioandroidtestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.navtureapps.proximiioandroid.Proximiio;
import com.navtureapps.proximiioandroid.ProximiioInput;
import com.navtureapps.proximiioandroid.ProximiioListener;

public class MapActivity extends Activity implements OnMapReadyCallback {
    private Proximiio proximiio;
    private GoogleMap map;
    private Marker iBeaconMarker;
    private Marker indoorAtlasMarker;
    private Marker gpsGeofenceMarker;
    private Marker steerPathMarker;
    private boolean centered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.main_bar, null);
            actionBar.setCustomView(cView);
        }

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centered = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Activity activity = this;

        ProximiioListener listener = new ProximiioListener() {
            @Override
            public void eventEnter(ProximiioInput input) {
                input.handleOutput(activity, null, true, false);
            }

            @Override
            public void eventLeave(ProximiioInput input) {
                input.handleOutput(activity, null, true, false);
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
            public void positionChange(final double lat, final double lon, final double accuracy, final ProximiioInput.InputType inputType) {
                if (map != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (inputType) {
                                case iBeacon:
                                    if (iBeaconMarker == null) {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(new LatLng(lat, lon))
                                                .title("iBeacon location")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                        iBeaconMarker = map.addMarker(marker);
                                    }
                                    else {
                                        iBeaconMarker.setPosition(new LatLng(lat, lon));
                                    }
                                    break;
                                case IndoorAtlas:
                                    if (indoorAtlasMarker == null) {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(new LatLng(lat, lon))
                                                .title("Indoor Atlas location")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        indoorAtlasMarker = map.addMarker(marker);
                                    }
                                    else {
                                        indoorAtlasMarker.setPosition(new LatLng(lat, lon));
                                    }
                                    break;
                                case GPSGeofence:
                                    if (gpsGeofenceMarker == null) {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(new LatLng(lat, lon))
                                                .title("Native location")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        gpsGeofenceMarker = map.addMarker(marker);
                                    }
                                    else {
                                        gpsGeofenceMarker.setPosition(new LatLng(lat, lon));
                                    }
                                    break;
                                case SteerPath:
                                    if (steerPathMarker == null) {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(new LatLng(lat, lon))
                                                .title("SteerPath location")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                        steerPathMarker = map.addMarker(marker);
                                    }
                                    else {
                                        steerPathMarker.setPosition(new LatLng(lat, lon));
                                    }
                                    break;
                            }
                            if (!centered) {
                                centered = true;
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13));
                            }
                        }
                    });
                }
            }
        };

        proximiio = new Proximiio(this, Settings.getAppID(this), Settings.getAuthToken(this), listener);
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

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(63, 26), 5.5f));
    }
}
