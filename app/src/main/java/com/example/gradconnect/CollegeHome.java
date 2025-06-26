package com.example.gradconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CollegeHome extends Fragment {

    private TextView textViewNotificationTitle;
    private TextView textViewNotificationContent;
    private TextView textViewNoticeTitle;
    private TextView textViewNoticeContent;

    public CollegeHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_college_home, container, false);

        // Initialize UI elements
        textViewNotificationTitle = view.findViewById(R.id.textView65);
        textViewNotificationContent = view.findViewById(R.id.textView66);
        textViewNoticeTitle = view.findViewById(R.id.textView61);
        textViewNoticeContent = view.findViewById(R.id.textView62);

        // Initialize ImageButton for menu
        ImageButton menuButton = view.findViewById(R.id.imageButton);
        menuButton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeCollegeActivity) {
                ((HomeCollegeActivity) getActivity()).openDrawer();
            }
        });

        ImageButton verificationButton = view.findViewById(R.id.imageButton4);
        verificationButton.setOnClickListener(v -> {
            // Start the activity for verification page
            Intent intent = new Intent(getActivity(), VerificationPageActivity.class);
            startActivity(intent);
        });

        ImageButton messagesButton = view.findViewById(R.id.imageButton5);
        messagesButton.setOnClickListener(v -> {
            // Start the activity for messages
            Intent intent = new Intent(getActivity(), MessagesAdminActivity.class);
            startActivity(intent);
        });

        // Fetch notifications and notices
        fetchRecentNotification();
        fetchRecentNotice();

        return view;
    }

    private void fetchRecentNotification() {
        String url = IPv4Connection.getBaseUrl() + "recent_notification.php"; // Replace with your PHP script URL

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                String title = data.getString("title");
                                String content = data.getString("content");

                                textViewNotificationTitle.setText(title);
                                textViewNotificationContent.setText(content);
                            } else if (response.has("message")) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchRecentNotice() {
        String url = IPv4Connection.getBaseUrl() + "recent_notice.php"; // Replace with your PHP script URL

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                String noticeTitle = data.getString("notice_title");
                                String noticeContent = data.getString("notice_content");

                                textViewNoticeTitle.setText(noticeTitle);
                                textViewNoticeContent.setText(noticeContent);
                            } else if (response.has("message")) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
