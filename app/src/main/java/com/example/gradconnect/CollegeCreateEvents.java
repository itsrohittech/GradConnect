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

public class CollegeCreateEvents extends Fragment {

    private static final String TAG = "CollegeCreateEvents";

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonPublish;
    private RecyclerView recyclerViewEvents;
    private CollegeEventsAdapter adapter;
    private ArrayList<JSONObject> eventsList;
    private ImageButton menuButton;

    public CollegeCreateEvents() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_college_create_events, container, false);

        // Initialize UI elements
        editTextTitle = view.findViewById(R.id.editText); // Ensure you have these IDs in your layout
        editTextContent = view.findViewById(R.id.editText2);
        buttonPublish = view.findViewById(R.id.button7);
        recyclerViewEvents = view.findViewById(R.id.eventsRecycler);
        menuButton = view.findViewById(R.id.imageButton);

        // Initialize the list and adapter
        eventsList = new ArrayList<>();
        adapter = new CollegeEventsAdapter(eventsList, getContext());
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEvents.setAdapter(adapter);

        // Set OnClickListener for the publish button
        buttonPublish.setOnClickListener(v -> publishEvent());

        // Fetch events to display
        fetchEvents();

        // Set OnClickListener to open the navigation drawer
        menuButton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeCollegeActivity) {
                ((HomeCollegeActivity) getActivity()).openDrawer();
            }
        });

        return view;
    }

    private void publishEvent() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the data being sent
        Log.d(TAG, "Publishing Event: Title=" + title + ", Content=" + content + ", Date=" + date);

        // Create JSON object with the data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("event_title", title);
            jsonBody.put("event_content", content);
            jsonBody.put("date", date);

            // Log the final JSON object for verification
            Log.d(TAG, "Final JSON Body: " + jsonBody.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error creating JSON data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, IPv4Connection.getBaseUrl() + "create_events.php", jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Publish Event Response: " + response.toString()); // Log the response
                        try {
                            if (response.has("status") && response.getString("status").equals("success")) {
                                Toast.makeText(getContext(), "Event published successfully!", Toast.LENGTH_SHORT).show();
                                // Optionally, clear the input fields or navigate away
                                editTextTitle.setText("");
                                editTextContent.setText("");
                                fetchEvents(); // Refresh the events list
                            } else {
                                Toast.makeText(getContext(), "Failed to publish event: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error publishing event", error);
                        Toast.makeText(getContext(), "Error publishing event", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchEvents() {
        String url = IPv4Connection.getBaseUrl() + "events_get.php"; // Replace with your PHP script URL

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Create a JSON array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Fetch Events Response: " + response.toString()); // Log the response
                        try {
                            eventsList.clear();
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    eventsList.add(response.getJSONObject(i));
                                }
                            } else {
                                Log.d(TAG, "No events found.");
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching events", error);
                        Toast.makeText(getContext(), "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }
}
