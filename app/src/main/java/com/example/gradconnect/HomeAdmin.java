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

public class HomeAdmin extends Fragment {

    private static final String ARG_MOBILE_NUMBER = "mobile_number";

    private String mobileNumber;

    private TextView textView65;
    private TextView textView66;
    private TextView textView61; // For notice title
    private TextView textView62; // For notice content

    public HomeAdmin() {
        // Required empty public constructor
    }

    public static HomeAdmin newInstance(String mobileNumber) {
        HomeAdmin fragment = new HomeAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        textView65 = view.findViewById(R.id.textView65);
        textView66 = view.findViewById(R.id.textView66);
        textView61 = view.findViewById(R.id.textView61); // Initialize textView61
        textView62 = view.findViewById(R.id.textView62); // Initialize textView62

        // Find the ImageButton and set an OnClickListener to open the drawer
        ImageButton openDrawerButton = view.findViewById(R.id.imageButton);
        openDrawerButton.setOnClickListener(v -> {
            HomeAdminActivity drawerController = (HomeAdminActivity) getActivity();
            if (drawerController != null) {
                drawerController.openDrawer();
            }
        });

        ImageButton button = view.findViewById(R.id.imageButton4);
        button.setOnClickListener(v -> {
            // Start the activity that hosts the AddAccountActivity
            Intent intent = new Intent(getActivity(), AddAccountActivity.class);
            startActivity(intent);
        });

        ImageButton imageButton5 = view.findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(v -> {
            // Start the activity that hosts the ChatAdminActivity
            Intent intent = new Intent(getActivity(), ChatAdminActivity.class);
            startActivity(intent);
        });

        fetchRecentNotification();
        fetchRecentNotice(); // Fetch recent notice data

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

                                textView65.setText(title);
                                textView66.setText(content);
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

                                textView61.setText(noticeTitle);
                                textView62.setText(noticeContent);
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
