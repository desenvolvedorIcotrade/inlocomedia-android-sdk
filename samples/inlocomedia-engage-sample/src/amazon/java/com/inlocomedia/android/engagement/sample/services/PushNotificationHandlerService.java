/*
 * PushNotificationHandlerService.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.PushMessage;
import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.activities.NotificationsActivity;
import com.inlocomedia.android.engagement.sample.utils.Device;
import com.inlocomedia.android.engagement.sample.utils.NotificationsManager;

import java.util.List;

public class PushNotificationHandlerService extends GcmListenerService {

    private static final String LOG_TAG = PushNotificationHandlerService.class.getSimpleName();

    // Intent action used in local broadcast
    public static final String ACTION_SNS_NOTIFICATION = "sns-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    private final static int NOTIFICATION_ID = 88442233;

    /**
     * Helper method to extract SNS message from bundle.
     *
     * @param data bundle
     * @return message string from SNS push notification
     */
    public static String getMessage(Bundle data) {
        String message = getFirebaseMessage(data);
        if (message == null) {
            message = getAmazonMessage(data);
        }
        return message;
    }

    private static String getFirebaseMessage(final Bundle bundle) {
        if (bundle.containsKey("notification")) {
            final Bundle notification = bundle.getBundle("notification");
            if (notification != null) {
                return notification.getString("body");
            }
        }
        return null;
    }

    private static String getAmazonMessage(final Bundle data) {
        // If a push notification is sent as plain text, then the message appears in "default".
        // Otherwise it's in the "message" for JSON format.
        return data.containsKey("default") ? data.getString("default") : data.getString("message", "");
    }

    private static boolean isForeground(Context context) {
        // Gets a list of running processes.
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();

        // On some versions of android the first item in the list is what runs in the foreground,
        // but this is not true on all versions.  Check the process importance to see if the app
        // is in the foreground.
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : tasks) {
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance
                && packageName.equals(appProcess.processName)) {
                return true;
            }
        }
        return false;
    }

    private void displayNotification(final String message) {
        Intent notificationIntent = new Intent(this, NotificationsActivity.class);
        notificationIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);

        // Display a notification with an icon, message as content, and default sound. It also
        // opens the app when the notification is clicked.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(
                R.drawable.ic_notification)
                .setContentTitle("Title only: The message is in the content")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

    private void broadcast(final String from, final Bundle data) {
        Intent intent = new Intent(ACTION_SNS_NOTIFICATION);
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs. For Set of keys use
     *             data.keySet().
     */
    @Override
    public void onMessageReceived(final String from, final Bundle data) {
        String message = getMessage(data);
        Log.d(LOG_TAG, "From: " + from);
        Log.d(LOG_TAG, "Message: " + message);

        final PushMessage pushContent = InLocoEngagement.decodeReceivedMessage(this.getApplicationContext(), message);
        if (pushContent != null) {
            NotificationsManager.getInstance().addNotification(this, pushContent);
            InLocoEngagement.presentNotification(this, pushContent, R.drawable.ic_notification, Device.getRandomNotificationId());
        }
    }
}
