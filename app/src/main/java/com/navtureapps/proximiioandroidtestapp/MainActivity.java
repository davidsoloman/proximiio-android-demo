package com.navtureapps.proximiioandroidtestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.navtureapps.proximiioandroid.Proximiio;
import com.navtureapps.proximiioandroid.ProximiioInput;
import com.navtureapps.proximiioandroid.ProximiioListener;

import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;

public class MainActivity extends Activity {
    private boolean background;
    private Proximiio proximiio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());

        background = true;

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.main_bar, null);
            actionBar.setCustomView(cView);
        }

        final Activity activity = this;

        findViewById(R.id.imageButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ScanActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MapActivity.class);
                startActivity(intent);
            }
        });
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
            public void safeBackground() {
                finish();
            }
        };

//      UNCOMMENT and EDIT lines below to use your Proximi.IO Application
//        Settings.setAppID(this, "Your App ID here!");
//        Settings.setAuthToken(this, "Your auth-token here!");

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
        if (background) {
            proximiio.stopActivity();
        }
        else {
            proximiio.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (background) {
            proximiio.stopActivity();
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exitmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                background = false;
                finish();
                break;
        }
        return true;
    }
}
