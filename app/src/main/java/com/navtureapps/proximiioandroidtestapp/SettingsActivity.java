package com.navtureapps.proximiioandroidtestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.navtureapps.proximiioandroid.Proximiio;
import com.navtureapps.proximiioandroid.ProximiioInput;
import com.navtureapps.proximiioandroid.ProximiioListener;

public class SettingsActivity extends Activity {
    private EditText appIDInput;
    private EditText authTokenInput;
    private TextView organizationText;
    private TextView anonymousText;
    private TextView apiKeyText;
    private TextView apiSecretText;
    private Proximiio proximiio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.main_bar, null);
            actionBar.setCustomView(cView);
        }

        final Activity activity = this;

        appIDInput = (EditText)findViewById(R.id.appidinput);
        authTokenInput = (EditText)findViewById(R.id.authtokeninput);
        organizationText = (TextView)findViewById(R.id.organizationidvalue);
        anonymousText = (TextView)findViewById(R.id.anonymousidvalue);
        apiKeyText = (TextView)findViewById(R.id.appkeyvalue);
        apiSecretText = (TextView)findViewById(R.id.appsecretvalue);

        findViewById(R.id.saveappid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appID = appIDInput.getText().toString();
                if (!appID.equals("")) {
                    Settings.setAppID(activity, appID);
                    proximiio.setAppID(appID);
                }
                else {
                    appIDInput.setText(Settings.getAppID(activity));
                }
            }
        });

        findViewById(R.id.saveauthtoken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authToken = authTokenInput.getText().toString();
                if (!authToken.equals("")) {
                    Settings.setAuthToken(activity, authToken);
                    proximiio.setAuthToken(authToken);
                }
                else {
                    authTokenInput.setText(Settings.getAuthToken(activity));
                }
            }
        });

        appIDInput.setText(Settings.getAppID(activity));
        authTokenInput.setText(Settings.getAuthToken(activity));
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
            public void apiKeyChange(final String organizationID, final String anonymousID, final String apiKey, final String apiSecret) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        organizationText.setText(organizationID);
                        anonymousText.setText(anonymousID);
                        apiKeyText.setText(apiKey);
                        apiSecretText.setText(apiSecret);
                    }
                });
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
}
