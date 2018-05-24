/*
 * DeviceTokens.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.ui.view_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class DeviceTokens extends BaseObservable {
    private String advertisingId;
    private String applicationId;
    private String userId;

    @Bindable
    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(final String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DeviceTokens {"
               + ", advertisingId='" + advertisingId + '\''
               + ", applicationId='" + applicationId + '\''
               + ", userId='" + userId + '\''
               + '}';
    }
}
