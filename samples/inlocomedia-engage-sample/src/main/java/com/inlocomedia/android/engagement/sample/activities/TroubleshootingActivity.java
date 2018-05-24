/*
 * TroubleshootingActivity.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.databinding.ActivityTroubleshootingBinding;
import com.inlocomedia.android.engagement.sample.ui.view_models.RequirementStatus;
import com.inlocomedia.android.engagement.sample.utils.Publisher;
import com.inlocomedia.android.engagement.sample.utils.RequirementsController;
import com.inlocomedia.android.engagement.sample.utils.RequirementsController.ServiceType;
import com.inlocomedia.android.engagement.sample.utils.Subscriber;

public class TroubleshootingActivity extends AppCompatActivity {
    public static final String MAILTO_CONTACT = "engage@inlocomedia.com";

    private RequirementStatus requirementStatus;
    private RequirementsController requirementsController;
    private ActivityTroubleshootingBinding viewBinder;

    private Subscriber<Boolean> deviceRegisteredSubscription;
    private Subscriber<Boolean> locationEnabledSubscription;
    private Subscriber<Boolean> wiFiEnabledSubscription;
    private Subscriber<Boolean> connectivityAvailableSubscription;
    private Subscriber<Boolean> permissionsAcceptedSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troubleshooting);
        viewBinder = DataBindingUtil.setContentView(this, R.layout.activity_troubleshooting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.troubleshooting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requirementStatus = new RequirementStatus();
        requirementsController = new RequirementsController(this);
        viewBinder.setRequirements(requirementStatus);

        deviceRegisteredSubscription = new Subscriber<Boolean>() {
            @Override
            public void update(final Publisher<Boolean> object, final Boolean data) {
                setStatusChanged(RequirementsController.ServiceType.DEVICE_REGISTERED, data);
            }
        };
        locationEnabledSubscription = new Subscriber<Boolean>() {
            @Override
            public void update(final Publisher<Boolean> object, final Boolean data) {
                setStatusChanged(RequirementsController.ServiceType.LOCATION, data);
            }
        };
        wiFiEnabledSubscription = new Subscriber<Boolean>() {
            @Override
            public void update(final Publisher<Boolean> object, final Boolean data) {
                setStatusChanged(RequirementsController.ServiceType.WIFI, data);
            }
        };
        connectivityAvailableSubscription = new Subscriber<Boolean>() {
            @Override
            public void update(final Publisher<Boolean> object, final Boolean data) {
                setStatusChanged(RequirementsController.ServiceType.CONNECTIVITY, data);
            }
        };
        permissionsAcceptedSubscription = new Subscriber<Boolean>() {
            @Override
            public void update(final Publisher<Boolean> object, final Boolean data) {
                setStatusChanged(RequirementsController.ServiceType.PERMISSIONS, data);
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        requirementsController.subscribe(ServiceType.DEVICE_REGISTERED, deviceRegisteredSubscription);
        requirementsController.subscribe(ServiceType.LOCATION, locationEnabledSubscription);
        requirementsController.subscribe(ServiceType.WIFI, wiFiEnabledSubscription);
        requirementsController.subscribe(ServiceType.CONNECTIVITY, connectivityAvailableSubscription);
        requirementsController.subscribe(ServiceType.PERMISSIONS, permissionsAcceptedSubscription);
    }

    @Override
    protected void onPause() {
        super.onPause();

        requirementsController.unsubscribe(ServiceType.DEVICE_REGISTERED, deviceRegisteredSubscription);
        requirementsController.unsubscribe(ServiceType.LOCATION, locationEnabledSubscription);
        requirementsController.unsubscribe(ServiceType.WIFI, wiFiEnabledSubscription);
        requirementsController.unsubscribe(ServiceType.CONNECTIVITY, connectivityAvailableSubscription);
        requirementsController.unsubscribe(ServiceType.PERMISSIONS, permissionsAcceptedSubscription);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setStatusChanged(final ServiceType serviceType, final boolean state) {
        switch (serviceType) {
            case DEVICE_REGISTERED:
                requirementStatus.setDeviceRegistered(state);
                break;
            case LOCATION:
                requirementStatus.setLocationEnabled(state);
                break;
            case WIFI:
                requirementStatus.setWiFiAvailable(state);
                break;
            case CONNECTIVITY:
                requirementStatus.setConnectivityAvailable(state);
                break;
            case PERMISSIONS:
                requirementStatus.setPermissionsAccepted(state);
                break;
        }
    }

    public void openContact(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ MAILTO_CONTACT });
        startActivity(Intent.createChooser(intent, "Send via..."));
    }
}
