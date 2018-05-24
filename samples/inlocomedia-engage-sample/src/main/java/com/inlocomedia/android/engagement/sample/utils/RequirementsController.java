/*
 * RequirementsController.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;

import com.inlocomedia.android.engagement.InLocoEngagement;

import java.util.HashMap;
import java.util.Map;

public class RequirementsController {

    public enum ServiceType {
        CONNECTIVITY, LOCATION, WIFI, PERMISSIONS, DEVICE_REGISTERED
    }

    public class ServiceStatus {
        final ServiceType type;
        final boolean state;

        public ServiceStatus(final ServiceType type, final boolean state) {
            this.type = type;
            this.state = state;
        }
    }

    private final static int REFRESH_INTERVAL = 3000;

    private WifiManager mWifiManager;
    private LocationManager mGpsManager;
    private ConnectivityManager mConnectivityManager;
    private HashMap<ServiceType, Publisher<Boolean>> mObservables;
    private HashMap<ServiceType, Boolean> mStates;
    private final Context mContext;
    private final Handler mHandler;
    private Runnable mRefreshRunnable;
    private boolean mRefreshing;

    public RequirementsController(final Context context) {
        mContext = context.getApplicationContext();
        mStates = new HashMap<>();
        mObservables = new HashMap<>();
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mGpsManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mHandler = new Handler(Looper.getMainLooper());
        mRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        };

        mObservables.put(ServiceType.WIFI, new Publisher<Boolean>());
        mObservables.put(ServiceType.LOCATION, new Publisher<Boolean>());
        mObservables.put(ServiceType.CONNECTIVITY, new Publisher<Boolean>());
        mObservables.put(ServiceType.PERMISSIONS, new Publisher<Boolean>());
        mObservables.put(ServiceType.DEVICE_REGISTERED, new Publisher<Boolean>());

        mStates.put(ServiceType.WIFI, false);
        mStates.put(ServiceType.LOCATION, false);
        mStates.put(ServiceType.CONNECTIVITY, false);
        mStates.put(ServiceType.PERMISSIONS, false);
        mStates.put(ServiceType.DEVICE_REGISTERED, false);
    }

    public void subscribe(final ServiceType type, final Subscriber<Boolean> subscriber) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final Publisher<Boolean> publisher = mObservables.get(type);
                if (!publisher.contains(subscriber)) {
                    publisher.addObserver(subscriber);
                    subscriber.update(publisher, mStates.get(type));
                }
                if (!mRefreshing) {
                    startRefreshing();
                }
            }
        });
    }

    public void unsubscribe(final ServiceType type, final Subscriber<Boolean> subscriber) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final Publisher<Boolean> observable = mObservables.get(type);
                if (observable.contains(subscriber)) {
                    observable.deleteObserver(subscriber);
                }
                if (!mRefreshing) {
                    startRefreshing();
                }
            }
        });
    }

    public boolean hasObservers() {
        for (Publisher<Boolean> publisher : mObservables.values()) {
            if (publisher.countObservers() != 0) {
                return true;
            }
        }
        return false;
    }

    public void startRefreshing() {
        updateServiceTypeState(ServiceType.WIFI, mWifiManager.isWifiEnabled());
        updateServiceTypeState(ServiceType.LOCATION, isLocationProviderEnabled());
        updateServiceTypeState(ServiceType.CONNECTIVITY, isNetworkAvailable(mConnectivityManager));
        updateServiceTypeState(ServiceType.PERMISSIONS, isLocationPermissionEnabled());
        updateServiceTypeState(ServiceType.DEVICE_REGISTERED, InLocoEngagement.isDeviceRegistered());

        for (Map.Entry<ServiceType, Publisher<Boolean>> entry : mObservables.entrySet()) {
            entry.getValue().notifyObservers(mStates.get(entry.getKey()));
        }

        mRefreshing = hasObservers();
        if (mRefreshing) {
            mHandler.postDelayed(mRefreshRunnable, REFRESH_INTERVAL);
        }
    }

    public boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isLocationPermissionEnabled() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void updateServiceTypeState(ServiceType serviceType, boolean newState) {
        if (newState != mStates.get(serviceType)) {
            mObservables.get(serviceType).setChanged();
        }
        mStates.put(serviceType, newState);
    }

    private boolean isLocationProviderEnabled() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                   mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                   mGpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return mGpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }
}