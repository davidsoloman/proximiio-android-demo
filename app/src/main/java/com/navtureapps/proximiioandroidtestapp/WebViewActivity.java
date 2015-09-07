package com.navtureapps.proximiioandroidtestapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.navtureapps.proximiioandroid.Proximiio;
import com.navtureapps.proximiioandroid.ProximiioInput;
import com.navtureapps.proximiioandroid.ProximiioListener;


public class WebViewActivity extends Activity {
    private Proximiio proximiio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.main_bar, null);
            actionBar.setCustomView(cView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Activity activity = this;
        final WebView webView = (WebView)findViewById(R.id.webview);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        ProximiioListener listener = new ProximiioListener() {
            @Override
            public void eventEnter(ProximiioInput input) {
                input.handleOutput(activity, webView, true, true);
            }

            @Override
            public void eventLeave(ProximiioInput input) {
                input.handleOutput(activity, webView, true, true);
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
        };

        proximiio = new Proximiio(this, Settings.getAppID(this), Settings.getAuthToken(this), listener);

        ProximiioInput event = proximiio.getLastEvent();
        if (event != null) {
            event.handleOutput(this, webView, false, true);
        }
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
