package com.example.gradconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {

    private static final String TAG = "StudentDetailsActivity";
    private RecyclerView recyclerView;
    private StudentDetailsAdapter adapter;
    private ArrayList<StudentDetails> studentDetailsList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.student_details);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerStudent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ArrayList and Adapter
        studentDetailsList = new ArrayList<>();
        adapter = new StudentDetailsAdapter(studentDetailsList);
        recyclerView.setAdapter(adapter);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton button = findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // SearchView functionality
        SearchView searchView = findViewById(R.id.searchView);

        // Set listener for SearchView when the query is submitted
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String mobileNumber = query.trim();
                if (!mobileNumber.isEmpty()) {
                    Log.d(TAG, "Query submitted: " + mobileNumber);
                    fetchStudentDetails(mobileNumber);  // Call the method to fetch the details
                } else {
                    Toast.makeText(StudentDetailsActivity.this, "Please enter a mobile number", Toast.LENGTH_SHORT).show();
                }
                return false;  // Return false to let the SearchView handle the keyboard
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Handle real-time search here if needed
                return false;
            }
        });
    }

    // Method to fetch student details
    private void fetchStudentDetails(String mobileNumber) {
        String url = IPv4Connection.getBaseUrl() + "student_details.php?mobile_number=" + mobileNumber;
        Log.d(TAG, "Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d(TAG, "Raw Response: " + response.toString());

                        // Check if the response contains the "data" key
                        if (response.has("data")) {
                            JSONObject dataObject = response.getJSONObject("data");
                            Log.d(TAG, "Response Data: " + dataObject.toString());

                            // Clear the list before adding new data
                            studentDetailsList.clear();

                            // Parse the response JSON
                            String name = dataObject.optString("first_name") + " " + dataObject.optString("last_name");
                            String age = dataObject.optString("age");
                            String gender = dataObject.optString("gender");
                            String collegeName = dataObject.optString("college_name");
                            String fieldOfStudy = dataObject.optString("field_of_study");
                            String mobileNumberRes = dataObject.optString("mobile_number");
                            String email = dataObject.optString("email");

                            // Get survey details
                            String question1 = dataObject.optString("question_1");
                            String answer1 = dataObject.optString("answer_1");
                            String question2 = dataObject.optString("question_2");
                            String answer2 = dataObject.optString("answer_2");
                            String question3 = dataObject.optString("question_3");
                            String answer3 = dataObject.optString("answer_3");
                            String question4 = dataObject.optString("question_4");
                            String answer4 = dataObject.optString("answer_4");
                            String question5 = dataObject.optString("question_5");
                            String answer5 = dataObject.optString("answer_5");

                            // Add data to the list
                            StudentDetails studentDetails = new StudentDetails(
                                    name, age, gender, collegeName, fieldOfStudy, mobileNumberRes, email,
                                    question1, answer1, question2, answer2, question3, answer3,
                                    question4, answer4, question5, answer5
                            );
                            studentDetailsList.add(studentDetails);

                            // Notify adapter of data change
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Student details updated in adapter");

                        } else {
                            Log.d(TAG, "No data key found in response");
                            Toast.makeText(StudentDetailsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        Toast.makeText(StudentDetailsActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            // Handle error
            Log.e(TAG, "Volley Error: " + error.getMessage());
            Toast.makeText(StudentDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        // Add the request to the request queue
        requestQueue.add(request);
    }
}
