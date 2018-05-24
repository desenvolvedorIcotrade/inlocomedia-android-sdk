package com.inlocomedia.android.engagement.sample;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.InLocoEngagementOptions;
import com.inlocomedia.android.engagement.RegisterDeviceResult;
import com.inlocomedia.android.engagement.request.FirebaseDeviceRegisterRequest;
import com.inlocomedia.android.engagement.request.RegisterDeviceCallback;
import com.inlocomedia.android.engagement.request.RegisterDeviceRequest;

public class EngageApplication extends Application {

    private static final String TAG = EngageApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * Initializing In Loco Engage SDK
         */
        InLocoEngagementOptions options = InLocoEngagementOptions.getInstance(this);
        options.setLogEnabled(this.getResources().getBoolean(R.bool.inloco_logs_enabled));
        options.setApplicationId(BuildConfig.ENGAGE_APPLICATION_ID);
        InLocoEngagement.init(this, options);

        String token = FirebaseInstanceId.getInstance().getToken();
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "setEndpoint() called with: pushProviderToken = [" + token + "]");
        }

        if (token != null && !token.isEmpty()) {
            RegisterDeviceRequest registerRequest = new FirebaseDeviceRegisterRequest.Builder()
                    .setFirebaseToken(token)
                    //.setUserId("<Your User Id>")
                    .build();

            // Registering device endpoint
            InLocoEngagement.registerDeviceForPushServices(this, registerRequest, new RegisterDeviceCallback() {
                @Override
                public boolean onDeviceRegisterCallback(final RegisterDeviceResult registerDeviceResult) {
                    if (!registerDeviceResult.isSuccess()) {
                        if (BuildConfig.DEBUG) Log.i(TAG, "Failed registering application. Check your log for more details");
                        Toast.makeText(getApplicationContext(), "Failed registering application. Check your log for more details", Toast.LENGTH_LONG).show();
                    } else {
                        if (BuildConfig.DEBUG) Log.i(TAG, "Finished registering successfully");
                        Toast.makeText(getApplicationContext(), "Finished registering successfully", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        }
    }
}
