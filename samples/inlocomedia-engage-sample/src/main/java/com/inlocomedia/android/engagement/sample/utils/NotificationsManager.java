/*
 * PushMessageManager.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import android.content.Context;
import android.util.Log;

import com.inlocomedia.android.engagement.PushMessage;
import com.inlocomedia.android.engagement.sample.adapters.NotificationStorageAdapter;
import com.inlocomedia.android.engagement.sample.models.Notification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NotificationsManager {
    public interface NotificationListener {
        void onListUpdated(final List<Notification> messages);
    }

    private static final String TAG = "NotificationActivity";
    private static final String FILENAME = "notifications.txt";

    private static NotificationsManager sInstance;
    private final NotificationStorageAdapter storageAdapter;

    private List<Notification> notifications;

    public static synchronized NotificationsManager getInstance() {
        if (sInstance == null) {
            sInstance = new NotificationsManager();
        }

        return sInstance;
    }

    private NotificationsManager() {
        storageAdapter = new NotificationStorageAdapter();
    }

    private void saveNotification(final Context context, final Notification notification) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TAG) {
                    File file = new File(context.getFilesDir(), FILENAME);
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                        storageAdapter.write(pw, notification);
                    } catch (IOException e) {
                        Log.e(TAG, "Could not write notification to file: ", e);
                    } finally {
                        if (pw != null) {
                            pw.close();
                        }
                    }
                }
            }
        }).start();
    }

    public void loadNotifications(final Context context, final NotificationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TAG) {
                    BufferedReader br = null;
                    Notification notification;
                    notifications = new ArrayList<>();

                    try {
                        File file = new File(context.getFilesDir(), FILENAME);
                        if (file.exists()) {
                            br = new BufferedReader(new FileReader(file));
                            while ((notification = storageAdapter.read(br)) != null) {
                                notifications.add(notification);
                            }
                        }

                        if (listener != null) {
                            listener.onListUpdated(notifications);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Could not read notifications from file: ", e);
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close BufferedReader: ", e);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void addNotification(Context context, PushMessage pushMessage) {
        Notification n = new Notification(System.currentTimeMillis(), pushMessage.getTitle(), pushMessage.getMessage(), null);
        saveNotification(context, n);
    }

}
