package com.example.gradconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private ArrayList<Notifications> createNotificationsList;

    // Constructor
    public NotificationsAdapter(ArrayList<Notifications> createNotificationsList) {
        this.createNotificationsList = createNotificationsList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_recycler, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // Get the Notification object for this position
        Notifications notification = createNotificationsList.get(position);
        // Bind data to the ViewHolder
        holder.titleTextView.setText(notification.getTitle());
        holder.contentTextView.setText(notification.getContent());
        holder.dateTextView.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return createNotificationsList.size();
    }

    // ViewHolder class to hold references to views
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView dateTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView60);
            contentTextView = itemView.findViewById(R.id.textView69);
            dateTextView = itemView.findViewById(R.id.textView64);
        }
    }
}
