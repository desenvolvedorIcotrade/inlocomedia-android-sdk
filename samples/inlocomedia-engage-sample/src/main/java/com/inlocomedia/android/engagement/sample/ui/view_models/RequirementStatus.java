/*
 * PermissionsStatus.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.ui.view_models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.inlocomedia.android.engagement.sample.BR;

public class RequirementStatus extends BaseObservable {
    private boolean deviceRegistered;
    private boolean locationEnabled;
    private boolean wiFiAvailable;
    private boolean connectivityAvailable;
    private boolean permissionsAccepted;

    @Bindable
    public boolean isDeviceRegistered() {
        return deviceRegistered;
    }

    public void setDeviceRegistered(final boolean deviceRegistered) {
        this.deviceRegistered = deviceRegistered;
        notifyPropertyChanged(BR.deviceRegistered);
    }

    @Bindable
    public boolean isLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(final boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
        notifyPropertyChanged(BR.locationEnabled);
    }

    @Bindable
    public boolean isWiFiAvailable() {
        return wiFiAvailable;
    }

    public void setWiFiAvailable(final boolean wiFiAvailable) {
        this.wiFiAvailable = wiFiAvailable;
        notifyPropertyChanged(BR.wiFiAvailable);
    }

    @Bindable
    public boolean isConnectivityAvailable() {
        return connectivityAvailable;
    }

    public void setConnectivityAvailable(final boolean connectivityAvailable) {
        this.connectivityAvailable = connectivityAvailable;
        notifyPropertyChanged(BR.connectivityAvailable);
    }

    @Bindable
    public boolean isPermissionsAccepted() {
        return permissionsAccepted;
    }

    public void setPermissionsAccepted(final boolean permissionsAccepted) {
        this.permissionsAccepted = permissionsAccepted;
        notifyPropertyChanged(BR.permissionsAccepted);
    }

}
