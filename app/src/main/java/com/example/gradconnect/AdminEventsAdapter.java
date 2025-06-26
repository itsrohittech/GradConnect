package com.example.gradconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdminEventsAdapter extends RecyclerView.Adapter<AdminEventsAdapter.EventViewHolder> {

    private List<JSONObject> events;

    public AdminEventsAdapter(List<JSONObject> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_recycler, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        JSONObject event = events.get(position);
        try {
            holder.titleTextView.setText(event.getString("event_title"));
            holder.contentTextView.setText(event.getString("event_content"));
        } catch (JSONException e) {
            e.printStackTrace();
            holder.titleTextView.setText("Error");
            holder.contentTextView.setText("Error");
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView ,textViewDate;
        TextView contentTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView60);
            contentTextView = itemView.findViewById(R.id.textView69);
            textViewDate = itemView.findViewById(R.id.textView64);
        }
    }
}
