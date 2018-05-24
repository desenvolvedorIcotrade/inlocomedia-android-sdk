/*
 * SettingsActivity.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inlocomedia.android.engagement.sample.BuildConfig;
import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.databinding.ActivitySettingsBinding;
import com.inlocomedia.android.engagement.sample.ui.view_models.DeviceTokens;
import com.inlocomedia.android.engagement.sample.utils.PushProviderToken;
import com.inlocomedia.android.engagement.sample.utils.PushProvidersManager;
import com.inlocomedia.android.engagement.sample.utils.TokenLoader;

public class SettingsActivity extends AppCompatActivity {

    public static final String URL_PRIVACY_POLICY = "https://www.inlocomedia.com/politica-de-privacidade/";
    public static final String URL_TERMS_OF_SERVICE = "https://www.inlocomedia.com/termos-de-uso/";
    public static final String MAILTO_CONTACT = "engage@inlocomedia.com";

    private DeviceTokens deviceTokens;

    private PushProvidersManager pushProvidersManager;
    private PushProvidersManager.TokenSubscription advertisingIdSubscription;
    private PushProvidersManager.TokenSubscription appIdSubscription;

    private View.OnTouchListener tvTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            TextView tv = (TextView) v;

            if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= (tv.getRight() - tv.getTotalPaddingRight())) {
                switch (tv.getId()) {
                    case R.id.tv_mad_id:
                        copyToClipboard(deviceTokens.getAdvertisingId());
                        break;

                    case R.id.tv_app_id:
                        copyToClipboard(deviceTokens.getApplicationId());
                        break;
                }
            }

            return true;
        }
    };

    private void copyToClipboard(final String id) {
        if (id != null && !id.isEmpty()) {
            ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(null, id));
            Toast.makeText(getApplicationContext(), R.string.copied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final ActivitySettingsBinding viewBinder = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deviceTokens = new DeviceTokens();
        viewBinder.setTokens(deviceTokens);
        pushProvidersManager = PushProvidersManager.getInstance(this);
        advertisingIdSubscription = new PushProvidersManager.TokenSubscription() {
            @Override
            public void onTokenUpdated(final String token, final PushProviderToken provider) {
                setText(PushProviderToken.ADVERTISING_ID, token);
            }
        };

        appIdSubscription = new PushProvidersManager.TokenSubscription() {
            @Override
            public void onTokenUpdated(final String token, final PushProviderToken provider) {
                setText(PushProviderToken.APP_ID, token);
            }
        };

        ((TextView) findViewById(R.id.tv_app_version)).setText(BuildConfig.VERSION_NAME);
        TextView tv = ((TextView) findViewById(R.id.tv_mad_id));
        tv.setOnTouchListener(tvTouchListener);

        tv = ((TextView) findViewById(R.id.tv_app_id));
        tv.setOnTouchListener(tvTouchListener);

        TokenLoader.reload(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pushProvidersManager.subscribe(PushProviderToken.ADVERTISING_ID, advertisingIdSubscription);
        pushProvidersManager.subscribe(PushProviderToken.APP_ID, appIdSubscription);
    }

    @Override
    protected void onStop() {
        super.onStop();
        pushProvidersManager.unsubscribe(PushProviderToken.ADVERTISING_ID, advertisingIdSubscription);
        pushProvidersManager.unsubscribe(PushProviderToken.APP_ID, appIdSubscription);
    }

    public void openLink(View view) {
        switch (view.getId()) {
            case R.id.tv_privacy_policy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PRIVACY_POLICY)));
                break;
            case R.id.tv_terms_of_service:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TERMS_OF_SERVICE)));
                break;
            case R.id.tv_contact:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ MAILTO_CONTACT });
                startActivity(Intent.createChooser(intent, "Send via..."));
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setText(final PushProviderToken tokenType, final String token) {
        switch (tokenType) {
            case ADVERTISING_ID:
                deviceTokens.setAdvertisingId(token);
                break;
            case APP_ID:
                deviceTokens.setApplicationId(token);
                break;
        }

        deviceTokens.notifyChange();
    }
}
