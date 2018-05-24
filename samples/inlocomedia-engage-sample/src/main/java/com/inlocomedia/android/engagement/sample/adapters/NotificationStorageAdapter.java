/*
 * NotificationStorageAdapter.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.adapters;

import com.inlocomedia.android.engagement.sample.models.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class NotificationStorageAdapter {

    public Notification read(final BufferedReader br) throws IOException {
        String line;

        if ((line = br.readLine()) == null) {
            return null;
        } else {
            return new Notification(Long.parseLong(line), br.readLine(), br.readLine(), null);
        }
    }

    public void write(final PrintWriter pw, final Notification notification) {
        pw.println(notification.getTimestamp());
        pw.println(notification.getTitle());
        pw.println(notification.getMessage());
    }
}
