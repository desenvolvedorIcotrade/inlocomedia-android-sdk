/*
 * OnboardingActivity.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inlocomedia.android.engagement.sample.R;

public class OnboardingActivity extends Activity {

    public static final String SHARED_PREFS_KEY = "shared_prefs";
    public static final String FIRST_RUN = "first_run";
    public static final String HAS_ACCOUNT = "has_account";
    public static final int PAGES = 4;

    /**
     * The PagerAdapter that will provide
     * fragments for each of the pages. We use a
     * FragmentPagerAdapter derivative, which will keep every
     * loaded fragment in memory.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The ViewPager that will host the section contents.
     */
    private ViewPager viewPager;

    private ImageView[] indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        boolean hasAccount = getIntent().getBooleanExtra(HAS_ACCOUNT, false);

        // Create the adapter that will return a fragment for each of the three
        // primary pages of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), PAGES, hasAccount);

        indicators = new ImageView[] { (ImageView) findViewById(R.id.indicator_0),
                                       (ImageView) findViewById(R.id.indicator_1),
                                       (ImageView) findViewById(R.id.indicator_2),
                                       (ImageView) findViewById(R.id.indicator_3) };

        final Button skipButton = (Button) findViewById(R.id.bt_skip);

        // Set up the ViewPager with the pages adapter and OnPageChangeListener.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {}

            @Override
            public void onPageSelected(final int position) {
                updateIndicators(position);
                skipButton.setText(position != PAGES - 1 ? R.string.skip : R.string.done);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {}
        });
    }

    private void updateIndicators(final int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
        }
    }

    public void finishOnboarding(View view) {
        getPrefs(this).edit().putBoolean(FIRST_RUN, false).apply();
        startActivity(new Intent(OnboardingActivity.this, NotificationsActivity.class));
        finish();
    }

    /**
     * A FragmentPagerAdapter that returns a fragment corresponding to
     * one of the pages/tabs.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int pages;
        private int[] textViews;
        private int[] imageViews;

        SectionsPagerAdapter(FragmentManager fm, final int pages, boolean hasAccount) {
            super(fm);

            this.pages = pages;
            this.textViews = new int[] { R.string.onboarding_welcome,
                                         hasAccount ? R.string.onboarding_with_account : R.string.onboarding_no_account,
                                         R.string.onboarding_notification,
                                         R.string.onboarding_finish };
            this.imageViews = new int[] { R.drawable.iv_cel,
                                          R.drawable.iv_store,
                                          R.drawable.iv_notification,
                                          R.drawable.iv_conclusion };
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a OnboardingCardFragment (defined as a static inner class below).
            return OnboardingCardFragment.newInstance(position, textViews[position], imageViews[position]);
        }

        @Override
        public int getCount() {
            return this.pages;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class OnboardingCardFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_TEXT_RESOURCE = "text_resource";
        private static final String ARG_IMAGE_RESOURCE = "image_resource";

        private TextView tv;
        private ImageView iv;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static OnboardingCardFragment newInstance(int sectionNumber, int textViewResId, int imageViewResId) {
            OnboardingCardFragment fragment = new OnboardingCardFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_TEXT_RESOURCE, textViewResId);
            args.putInt(ARG_IMAGE_RESOURCE, imageViewResId);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);

            tv = (TextView) rootView.findViewById(R.id.tv_onboarding);
            tv.setText(getArguments().getInt(ARG_TEXT_RESOURCE));

            iv = (ImageView) rootView.findViewById(R.id.iv_onboarding);
            iv.setImageResource(getArguments().getInt(ARG_IMAGE_RESOURCE));

            return rootView;
        }

    }

    private static SharedPreferences getPrefs(final Context context) {
        return context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }
}
