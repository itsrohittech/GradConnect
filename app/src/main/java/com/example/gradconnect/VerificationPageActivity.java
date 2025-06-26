package com.example.gradconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerificationPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AlumniVeri> alumniList;
    private AlumniVeriAdapter adapter;
    private static final String TAG = "VerificationPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_page);

        // Set up window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        alumniList = new ArrayList<>();  // Initialize alumniList before passing it to the adapter
        adapter = new AlumniVeriAdapter(alumniList, this);  // Pass 'this' as context
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch the alumni data from API
        fetchAlumniDetails();

        // Set up button to finish activity
        ImageButton button = findViewById(R.id.imageButton);
        button.setOnClickListener(v -> finish());
    }

    private void fetchAlumniDetails() {
        String url = IPv4Connection.getBaseUrl() + "alumni_veri_get.php"; // Update with the correct API endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        alumniList.clear(); // Clear the list before adding new items
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject alumniObject = response.getJSONObject(i);

                            // Create AlumniVeri object from the JSON data
                            AlumniVeri alumni = new AlumniVeri(
                                    alumniObject.getString("name"),
                                    alumniObject.getString("dob"),
                                    alumniObject.getString("gender"),
                                    alumniObject.getString("college_name"),
                                    alumniObject.getString("field_of_study"),
                                    alumniObject.getString("dept"),
                                    alumniObject.getString("mobile_number"),
                                    alumniObject.getString("email")
                            );

                            // Add the alumni to the list
                            alumniList.add(alumni);
                        }

                        // Notify the adapter of the data changes
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON data", e);
                    }
                },
                error -> Log.e(TAG, "Error fetching alumni details", error)
        );

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }
}
