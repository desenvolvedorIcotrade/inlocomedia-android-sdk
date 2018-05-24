/*
 * InstallReceiver.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.inlocomedia.android.engagement.sample.BuildConfig;
import com.inlocomedia.android.engagement.sample.utils.PushProviderToken;
import com.inlocomedia.android.engagement.sample.utils.PushProvidersManager;

public class InstallReceiver extends BroadcastReceiver {

    @SuppressWarnings("FieldCanBeLocal")
    private static String TAG = "Install Receiver";
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String KEY_REFERRER = "referrer";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            if (BuildConfig.DEBUG) Log.e(TAG, "Error: Null intent received");
            return;
        }

        if (BuildConfig.DEBUG) Log.i(TAG, "Intent received with action " + intent.getAction());

        if (ACTION_INSTALL_REFERRER.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                String appId = extras.getString(KEY_REFERRER);

                if (appId != null) {
                    if (BuildConfig.DEBUG) Log.i(TAG, "appId retrieved with value " + appId);
                    PushProvidersManager.getInstance(context).setToken(context, PushProviderToken.APP_ID, appId);
                }
            }
        }
    }
}
