package com.example.gradconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class CreateNotificationActivity extends AppCompatActivity {

    private static final String TAG = "CreateNotificationActivity";

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonPublish;
    private RecyclerView recyclerViewNotices;
    private NotificationsAdapter adapter;
    private ArrayList<Notifications> createNotificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_notification);

        // Initialize UI elements
        editTextTitle = findViewById(R.id.editText);
        editTextContent = findViewById(R.id.editText2);
        buttonPublish = findViewById(R.id.button7);
        recyclerViewNotices = findViewById(R.id.notificationsRecyclerView);

        // Initialize the list and adapter
        createNotificationsList = new ArrayList<>();
        adapter = new NotificationsAdapter(createNotificationsList);
        recyclerViewNotices.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotices.setAdapter(adapter);

        // Set padding for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set OnClickListener for publish button
        buttonPublish.setOnClickListener(v -> publishNotification());

        // Set OnClickListener for the close button
        ImageButton buttonClose = findViewById(R.id.imageButton);
        buttonClose.setOnClickListener(v -> finish());

        // Fetch existing notifications to display
        fetchNotifications();
    }

    // Function to publish a new notification
    private void publishNotification() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("content", content);
            jsonBody.put("date", date);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON data", e);
            Toast.makeText(this, "Error creating JSON data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                IPv4Connection.getBaseUrl() + "notifications_post.php",
                jsonBody,
                response -> {
                    Log.d(TAG, "Publish Notification Response: " + response.toString()); // Log the response
                    try {
                        if (response.has("status") && response.getString("status").equals("success")) {
                            Toast.makeText(this, "Notification published successfully!", Toast.LENGTH_SHORT).show();
                            // Clear the input fields
                            editTextTitle.setText("");
                            editTextContent.setText("");

                            // Refresh the list and finish the activity
                            fetchNotifications();
                            finish(); // Finish the activity
                        } else {
                            Toast.makeText(this, "Failed to publish notification: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error publishing notification", error);
                    Toast.makeText(this, "Error publishing notification", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }




    // Function to fetch existing notifications
    private void fetchNotifications() {
        String url = IPv4Connection.getBaseUrl()+"notifications_get.php"; // PHP endpoint for fetching notifications

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Fetch Notifications Response: " + response.toString()); // Log the response
                    try {
                        createNotificationsList.clear();
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject notificationObject = response.getJSONObject(i);
                                // Parse JSON into a Notifications object
                                Notifications notification = new Notifications(
                                        notificationObject.getString("title"),
                                        notificationObject.getString("content"),
                                        notificationObject.getString("date")
                                );
                                createNotificationsList.add(notification);
                            }
                        } else {
                            Log.d(TAG, "No notifications found.");
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching notifications", error);
                    Toast.makeText(this, "Error fetching notifications", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }
}
