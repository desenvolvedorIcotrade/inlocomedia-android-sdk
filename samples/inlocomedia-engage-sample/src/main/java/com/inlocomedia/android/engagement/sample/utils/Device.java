/*
 * Device.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Device {

    private Device() {}

    public static int getRandomNotificationId() {
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(new Date()));
    }
}
