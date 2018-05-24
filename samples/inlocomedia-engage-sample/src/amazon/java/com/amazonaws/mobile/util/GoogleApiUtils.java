package com.amazonaws.mobile.util;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

@SuppressWarnings("ALL")
public class GoogleApiUtils {
    public static boolean isGooglePlayServicesAvailable(Context context) {
        return GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }
}
