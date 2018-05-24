/*
 * InstanceIdHandlerService.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.services;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.RegisterDeviceResult;
import com.inlocomedia.android.engagement.request.FirebaseDeviceRegisterRequest;
import com.inlocomedia.android.engagement.request.RegisterDeviceCallback;
import com.inlocomedia.android.engagement.request.RegisterDeviceRequest;
import com.inlocomedia.android.engagement.sample.BuildConfig;
import com.inlocomedia.android.engagement.sample.EngageApplication;
import com.inlocomedia.android.engagement.sample.utils.PushProviderToken;
import com.inlocomedia.android.engagement.sample.utils.PushProvidersManager;

public class InstanceIdHandlerService extends FirebaseInstanceIdService {

    private static final String TAG = InstanceIdHandlerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "setEndpoint() called with: pushProviderToken = [" + token + "]");
        }

        RegisterDeviceRequest registerRequest = new FirebaseDeviceRegisterRequest.Builder()
                .setFirebaseToken(token)
                //.setUserId("<Your User Id>")
                .build();

        // Registering device endpoint
        if (token != null && !token.isEmpty()) {
            InLocoEngagement.registerDeviceForPushServices(this, registerRequest, new RegisterDeviceCallback() {
                @Override
                public boolean onDeviceRegisterCallback(final RegisterDeviceResult registerDeviceResult) {
                    if (!registerDeviceResult.isSuccess()) {
                        if (BuildConfig.DEBUG) Log.i(TAG, "Failed registering application. Check your log for more details");
                        Toast.makeText(getApplicationContext(), "Failed registering application. Check your log for more details", Toast.LENGTH_LONG).show();
                    } else {
                        if (BuildConfig.DEBUG) Log.i(TAG, "Finished registering successfully");
                        Toast.makeText(getApplicationContext(), "Finished registering successfully", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        }
    }
}