/*
 * Application.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */
package com.inlocomedia.android.engagement.sample;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;
import com.inlocomedia.android.engagement.sample.utils.InLocoHelper;

public class Application extends MultiDexApplication {
    private static final String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Remove later. Temporary while the Amazon register ARN event does not have context
        InLocoHelper.setContext(getApplicationContext());
        initAWSServices();
    }

    private void initAWSServices() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
        PushManager.setPushStateListener(new PushManager.PushStateListener() {
            @Override
            public void onPushStateChange(final PushManager pushManager, boolean isEnabled) {
                Log.d(LOG_TAG, "Push Notifications Enabled = " + isEnabled);
            }
        });
    }
}
