/*
 * NotificationsActivity.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.adapters.NotificationsAdapter;
import com.inlocomedia.android.engagement.sample.models.Notification;
import com.inlocomedia.android.engagement.sample.utils.InLocoHelper;
import com.inlocomedia.android.engagement.sample.utils.NotificationsManager;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @SuppressWarnings("unused")
    private static final String TAG = "NotificationsActivity";

    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private SwipeRefreshLayout refreshLayout;
    private TextView textView;

    private List<Notification> notifications;
    private NotificationsManager notificationsManager;

    private NotificationsManager.NotificationListener notificationListener;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // retrieve Toolbar and remove title/set logo
        Toolbar toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo_engage_blue);

        /*
         * Requesting necessary permissions
         */
        InLocoHelper.requestRequiredPermissions(this);

        textView = (TextView) findViewById(R.id.tv_your_notifications);
        notificationsManager = NotificationsManager.getInstance();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        // set up RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.notifications_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        notifications = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, notifications);
        recyclerView.setAdapter(notificationsAdapter);

        notificationListener = new NotificationsManager.NotificationListener() {
            @Override
            public void onListUpdated(final List<Notification> messages) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setEnabled(true);
                        refreshLayout.setRefreshing(false);
                        if (!messages.isEmpty()) {
                            textView.setVisibility(View.GONE);
                            notifications.clear();
                            notifications.addAll(messages);
                            notificationsAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(messages.size() - 1);
                        }
                    }
                });
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        notificationsManager.loadNotifications(this, notificationListener);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_item_troubleshooting:
                startActivity(new Intent(this, TroubleshootingActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setEnabled(false);
        notificationsManager.loadNotifications(this, notificationListener);
    }
}
