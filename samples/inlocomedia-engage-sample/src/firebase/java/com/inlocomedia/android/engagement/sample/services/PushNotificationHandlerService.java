/*
 * PushNotificationHandlerService.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.PushMessage;
import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.utils.NotificationsManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PushNotificationHandlerService extends FirebaseMessagingService {

    private static final String TAG = "InLocoMedia";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            Log.i(TAG, "Message data: " + data);

            // Decoding the content as a In Loco Push Message
            final PushMessage pushContent = InLocoEngagement.decodeReceivedMessage(this.getApplicationContext(), data);

            if (pushContent != null) {
                NotificationsManager.getInstance().addNotification(this, pushContent);
                InLocoEngagement.presentNotification(this,
                                                     pushContent,
                                                     R.drawable.ic_notification);
            }
        }
    }
}
