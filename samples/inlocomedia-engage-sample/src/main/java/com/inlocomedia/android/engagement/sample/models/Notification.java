/*
 * Notification.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.models;

public class Notification {
    private long timestamp;
    private String title;
    private String message;
    private String iconUrl;

    public Notification(final long timestamp, final String title, final String message, final String iconUrl) {
        this.timestamp = timestamp;
        this.title = title;
        this.message = message;
        this.iconUrl = iconUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
