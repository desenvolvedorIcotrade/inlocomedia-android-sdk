/*
 * NotificationsAdapter.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inlocomedia.android.engagement.sample.R;
import com.inlocomedia.android.engagement.sample.models.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notification> mList;
    private Context mContext;

    public NotificationsAdapter(final Context context, final List<Notification> notifications) {
        this.mContext = context;
        this.mList = notifications;
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Notification n = mList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(n.getTimestamp()));

        holder.tvTime.setText(date);
        holder.ivIcon.setImageResource(R.drawable.ic_avatar_card);
        holder.tvTitle.setText(n.getTitle());
        holder.tvMessage.setText(n.getMessage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvMessage;

        ViewHolder(final View itemView) {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

}
