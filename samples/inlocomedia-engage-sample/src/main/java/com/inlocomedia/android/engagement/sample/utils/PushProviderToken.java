/*
 * PushProviderToken.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

public enum PushProviderToken {
    GCM("gcm"), PUSH_PROVIDER_TOKEN("push_provider"), ADVERTISING_ID("advertising_id"), APP_ID("app_id");

    private String value;

    PushProviderToken(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}