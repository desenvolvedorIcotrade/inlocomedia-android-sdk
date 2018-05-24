/*
 * SplashActivity.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.inlocomedia.android.engagement.sample.R;

public class SplashActivity extends Activity {

    public static final String TAG = "SplashActivity";
    private static final String SHARED_PREFS_KEY = "shared_prefs";
    public static final String FIRST_RUN = "first_run";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!getPrefs(this).getBoolean(FIRST_RUN, true)) {
            startActivity(new Intent(SplashActivity.this, NotificationsActivity.class));
            finish();
        } else {
            Intent i = new Intent(this, OnboardingActivity.class);
            startActivity(i);
            finish();
        }
    }

    private static SharedPreferences getPrefs(final Context context) {
        return context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }
}
