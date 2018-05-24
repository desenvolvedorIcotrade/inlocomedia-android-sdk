/*
 * PushProvidersManager.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.inlocomedia.android.core.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PushProvidersManager {

    public interface TokenSubscription {
        void onTokenUpdated(final String token, final PushProviderToken provider);
    }

    private static final String SHARED_PREFS_KEY_DEVICE_TOKEN = "com.inlocomedia.android.engagement.sample.utils.PushProvidersToken";
    public static PushProvidersManager sInstance;
    private final Handler mHandler;

    private HashMap<PushProviderToken, String> mTokens;
    private HashMap<PushProviderToken, List<TokenSubscription>> mObserversHash;

    public static synchronized PushProvidersManager getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PushProvidersManager(context);
        }
        return sInstance;
    }

    public PushProvidersManager(final Context context) {
        mObserversHash = new HashMap<>();
        mTokens = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        loadToken(context, PushProviderToken.GCM);
        loadToken(context, PushProviderToken.PUSH_PROVIDER_TOKEN);
        loadToken(context, PushProviderToken.ADVERTISING_ID);
        loadToken(context, PushProviderToken.APP_ID);
    }

    public synchronized void subscribe(final PushProviderToken provider, final TokenSubscription subscription) {
        List<TokenSubscription> tokenSubscriptions = mObserversHash.get(provider);
        if (tokenSubscriptions == null) {
            tokenSubscriptions = new ArrayList<>();
        }
        tokenSubscriptions.add(subscription);
        mObserversHash.put(provider, tokenSubscriptions);

        final String token = mTokens.get(provider);
        if (token != null && !token.isEmpty()) {
            subscription.onTokenUpdated(token, provider);
        }
    }

    public synchronized void unsubscribe(final PushProviderToken provider, final TokenSubscription subscription) {
        final List<TokenSubscription> list = mObserversHash.get(provider);
        if (list != null) {
            list.remove(subscription);
        }
    }

    public synchronized String getToken(final PushProviderToken provider) {
        return mTokens.get(provider);
    }

    public synchronized void setToken(final Context context, final PushProviderToken provider, final String token) {
        if (!token.equals(mTokens.get(provider))) {
            mTokens.put(provider, token);
            saveToken(context, provider, token);
            notify(provider, token);
        }
    }

    public void notify(final PushProviderToken provider, final String token) {
        final List<TokenSubscription> list = mObserversHash.get(provider);
        if (list != null && !list.isEmpty()) {
            final List<TokenSubscription> observers = new ArrayList<>(list);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (final TokenSubscription observer : observers) {
                        observer.onTokenUpdated(token, provider);
                    }
                }
            });
        }
    }

    private void saveToken(final Context context, final PushProviderToken provider, @NonNull final String newToken) {
        Validator.notNullNorEmpty(newToken, provider + " token");
        getPrefs(context).edit().putString(provider.toString(), newToken).apply();
    }

    private void loadToken(final Context context, final PushProviderToken provider) {
        final String token = getPrefs(context).getString(provider.toString(), null);
        if (token != null && !token.isEmpty()) {
            mTokens.put(provider, token);
        }
    }

    private static SharedPreferences getPrefs(final Context context) {
        return context.getSharedPreferences(SHARED_PREFS_KEY_DEVICE_TOKEN, Context.MODE_PRIVATE);
    }
}
