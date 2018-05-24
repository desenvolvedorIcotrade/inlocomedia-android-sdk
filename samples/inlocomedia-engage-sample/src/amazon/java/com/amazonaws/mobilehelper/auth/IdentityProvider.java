package com.amazonaws.mobilehelper.auth;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.17
//

import android.content.Context;

import com.amazonaws.mobilehelper.auth.user.IdentityProfile;
import com.amazonaws.mobilehelper.config.AWSMobileHelperConfiguration;

/**
 * Interface sign-in provider's supported by the IdentityManager must implement.
 */
@SuppressWarnings("ALL")
public interface IdentityProvider {

    /**
     * Method called upon constructing an identity provider for it to handle its initialization.
     *
     * @param context the context.
     * @param configuration the configuration.
     */
    void initialize(Context context, AWSMobileHelperConfiguration configuration);

    /**
     * @return the Identity Provider Type.
     */
    IdentityProviderType getProviderType();

    /**
     * @return the display name for this provider.
     */
    String getDisplayName();

    /**
     * @return the key used by Cognito in its login map when refreshing credentials.
     */
    String getCognitoLoginKey();

    /**
     * Refreshes the state of whether the user is signed-in and returns the updated state.
     * Note: This call may block, so it must not be called from the main thread.
     * @return true if signed in with this provider, otherwise false.
     */
    boolean refreshUserSignInState();

    /**
     * Call getToken to retrieve the access token from successful sign-in with this provider.
     * Note: This call may block if the access token is not already cached.
     * @return the access token suitable for use with Cognito.
     */
    String getToken();

    /**
     * Refreshes the token if it has expired.
     * Note: this call may block due to network access, and must be called from a background thread.
     * @return the refreshed access token, or null if the token cannot be refreshed.
     */
    String refreshToken();

    /**
     * Call signOut to sign out of this provider.
     */
    void signOut();

    /**
     * @return the identity profile class that may be used to obtain the profile for the signed-in
     * user for this provider.
     */
    Class<? extends IdentityProfile> getIdentityProfileClass();
}