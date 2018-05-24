/*
 * InLocoHelper.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.inlocomedia.android.core.permissions.PermissionResult;
import com.inlocomedia.android.core.permissions.PermissionsListener;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.sample.R;

import java.util.HashMap;

public class InLocoHelper {

    private final static String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                                                          Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "InLocoHelper";

    private InLocoHelper() {}

    public static void requestRequiredPermissions(final Activity activity) {
        final boolean askIfDenied = true;
        InLocoEngagement.requestPermissions(activity, REQUIRED_PERMISSIONS, askIfDenied, new PermissionsListener() {
            @Override
            public void onPermissionRequestCompleted(final HashMap<String, PermissionResult> permissionResult) {
                PermissionResult fineLocationResult = permissionResult.get(Manifest.permission.ACCESS_FINE_LOCATION);
                if (fineLocationResult == null ||
                    (!fineLocationResult.isAuthorized() && fineLocationResult.hasChanged())) {
                    Toast.makeText(activity, R.string.location_refused, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static boolean isDeviceRegistered() {
        return InLocoEngagement.isDeviceRegistered();
    }
}
