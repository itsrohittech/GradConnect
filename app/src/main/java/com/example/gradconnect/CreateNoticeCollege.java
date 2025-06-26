package com.example.gradconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateNoticeCollege extends Fragment {

    private static final String TAG = "CreateNoticeCollege";

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonPublish;
    private RecyclerView recyclerViewNotices;
    private CollegeNoticesAdapter adapter;
    private ArrayList<JSONObject> noticeList;
    private RequestQueue requestQueue;

    public CreateNoticeCollege() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_notice_college, container, false);

        // Initialize UI elements
        editTextTitle = view.findViewById(R.id.editText);
        editTextContent = view.findViewById(R.id.editText2);
        buttonPublish = view.findViewById(R.id.button7);
        recyclerViewNotices = view.findViewById(R.id.NoticesRecycler);

        // Initialize the list and adapter
        noticeList = new ArrayList<>();
        adapter = new CollegeNoticesAdapter(noticeList);
        recyclerViewNotices.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotices.setAdapter(adapter);

        // Set OnClickListener for publish button
        buttonPublish.setOnClickListener(v -> publishNotice());

        // Set OnClickListener for the drawer button
        ImageButton openDrawerButton = view.findViewById(R.id.imageButton);
        openDrawerButton.setOnClickListener(v -> {
            HomeCollegeActivity drawerController = (HomeCollegeActivity) getActivity();
            if (drawerController != null) {
                drawerController.openDrawer();
            }
        });

        // Fetch existing notices to display
        fetchNotices();

        return view;
    }

    // Function to publish a new notice
    private void publishNotice() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the data being sent
        Log.d(TAG, "Publishing Notice: Title=" + title + ", Content=" + content + ", Date=" + date);

        // Create JSON object with the data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("notice_title", title);
            jsonBody.put("notice_content", content);
            jsonBody.put("notice_date", date);

            // Log the final JSON object for verification
            Log.d(TAG, "Final JSON Body: " + jsonBody.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON data", e);
            Toast.makeText(getContext(), "Error creating JSON data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request queue
        requestQueue = Volley.newRequestQueue(getContext());

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                IPv4Connection.getBaseUrl() + "create_notices.php",
                jsonBody,
                response -> {
                    Log.d(TAG, "Publish Notice Response: " + response.toString()); // Log the response
                    try {
                        if (response.has("status") && response.getString("status").equals("success")) {
                            Toast.makeText(getContext(), "Notice published successfully!", Toast.LENGTH_SHORT).show();
                            // Optionally, clear the input fields or navigate away
                            editTextTitle.setText("");
                            editTextContent.setText("");
                            fetchNotices(); // Refresh the list
                        } else {
                            Toast.makeText(getContext(), "Failed to publish notice: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error publishing notice", error);
                    Toast.makeText(getContext(), "Error publishing notice", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    // Function to fetch existing notices
    private void fetchNotices() {
        String url = IPv4Connection.getBaseUrl() + "notices_get.php"; // PHP endpoint for fetching notices

        // Create a request queue
        requestQueue = Volley.newRequestQueue(getContext());

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Fetch Notices Response: " + response.toString()); // Log the response
                    try {
                        noticeList.clear();
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject noticeObject = response.getJSONObject(i);
                                noticeList.add(noticeObject);
                            }
                        } else {
                            Log.d(TAG, "No notices found.");
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching notices", error);
                    Toast.makeText(getContext(), "Error fetching notices", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }
}
