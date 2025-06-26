package com.example.gradconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminNoticesAdapter extends RecyclerView.Adapter<AdminNoticesAdapter.NoticeViewHolder> {

    private ArrayList<JSONObject> noticeList;

    public AdminNoticesAdapter(ArrayList<JSONObject> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notices_recycler, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        JSONObject notice = noticeList.get(position);
        try {
            holder.noticeTitle.setText(notice.getString("notice_title"));
            holder.noticeContent.setText(notice.getString("notice_content"));
            holder.noticeDate.setText(notice.getString("notice_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTitle, noticeContent, noticeDate;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.textView60);
            noticeContent = itemView.findViewById(R.id.textView69);
            noticeDate = itemView.findViewById(R.id.textView64);
        }
    }
}
