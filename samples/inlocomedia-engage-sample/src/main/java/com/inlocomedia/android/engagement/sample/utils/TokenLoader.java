/*
 * GCMTokenLoader.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.inlocomedia.android.engagement.sample.R;

import java.io.IOException;

public class TokenLoader {

    private static final String TAG = "TokenLoader";

    private InstanceID mGCMInstanceId;
    private String mGCMDefaultSenderId;
    private PushProvidersManager mPushProvidersManager;
    private Context mContext;

    private TokenLoader(final Context context) {
        mContext = context;
        mGCMInstanceId = InstanceID.getInstance(context);
        mGCMDefaultSenderId = context.getResources().getString(R.string.gcm_defaultSenderId);
        mPushProvidersManager = PushProvidersManager.getInstance(context);
    }

    public static void reload(final Context context) {
        new TokenLoader(context).reloadGcmToken();
        new TokenLoader(context).reloadAdvertisingId();
    }

    private void reloadGcmToken() {
        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    String newDeviceToken = mGCMInstanceId.getToken(mGCMDefaultSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    mPushProvidersManager.setToken(mContext, PushProviderToken.GCM, newDeviceToken);
                } catch (Exception e) {
                    Log.w(TAG, "Failed to load GcmToken", e);
                }
            }
        })).start();
    }

    private void reloadAdvertisingId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info idInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
                    mPushProvidersManager.setToken(mContext, PushProviderToken.ADVERTISING_ID, idInfo.getId());
                    Log.i(TAG, "Advertising id: " + idInfo.getId());
                } catch (Exception e) {
                    Log.w(TAG, "Failed to load Advertising Id", e);
                }
            }
        }).start();
    }
}
