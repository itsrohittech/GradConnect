package com.example.gradconnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;

public class AlumniVerificationActivity extends AppCompatActivity {

    private TextView textViewFirstName, textViewAge, textViewGender, textViewCollegeName,
            textViewFieldOfStudy, textViewDept, textViewGraduationYear,
            textViewMobileNumber, textViewEmail, textViewStudentId;

    private DataHolder dataHolder = DataHolder.getInstance();
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.alumni_verification);

        // Initialize TextView elements
        textViewFirstName = findViewById(R.id.textView29);
        textViewAge = findViewById(R.id.textView30);
        textViewGender = findViewById(R.id.textView31);
        textViewCollegeName = findViewById(R.id.textView33);
        textViewFieldOfStudy = findViewById(R.id.textView42);
        textViewDept = findViewById(R.id.textView44);
        textViewGraduationYear = findViewById(R.id.textView45);
        textViewMobileNumber = findViewById(R.id.textView46);
        textViewEmail = findViewById(R.id.textView47);
        textViewStudentId = findViewById(R.id.textView48);

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup the back button
        ImageButton buttonBack = findViewById(R.id.imageButton);
        buttonBack.setOnClickListener(v -> finish());

        // Retrieve mobile number from Intent extras
        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobile_number");

        // If mobile number is not found in extras, try to get it from DataHolder (as a fallback)
        if (mobileNumber == null) {
            mobileNumber = dataHolder.getMobileNumber();
        }

        // Fetch details using mobileNumber
        if (mobileNumber != null) {
            new FetchDetailsTask().execute(mobileNumber);
        } else {
            Log.e("AlumniVerification", "No mobile number provided");
            // You could also display an error message here if no mobile number is found
        }
    }

    private class FetchDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String urlString = IPv4Connection.getBaseUrl()+"alumni_details_get.php?mobile_number=" + mobileNumber;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            } catch (Exception e) {
                Log.e("FetchDetailsTask", "Error fetching details", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String firstName = jsonObject.optString("first_name", "N/A");
                        String dob = jsonObject.optString("dob", "N/A");
                        String gender = jsonObject.optString("gender", "N/A");
                        String collegeName = jsonObject.optString("college_name", "N/A");
                        String fieldOfStudy = jsonObject.optString("field_of_study", "N/A");
                        String dept = jsonObject.optString("dept", "N/A");
                        String graduationYear = jsonObject.optString("graduation_year", "N/A");
                        String email = jsonObject.optString("email", "N/A");
                        String studentId = jsonObject.optString("student_id", "N/A");

                        // Set the text for each TextView
                        textViewFirstName.setText(firstName);
                        textViewGender.setText(gender);
                        textViewCollegeName.setText(collegeName);
                        textViewFieldOfStudy.setText(fieldOfStudy);
                        textViewDept.setText(dept);
                        textViewGraduationYear.setText(graduationYear);
                        textViewMobileNumber.setText(mobileNumber);
                        textViewEmail.setText(email);
                        textViewStudentId.setText(studentId);

                        // Calculate and set age from DOB
                        if (!dob.equals("N/A")) {
                            LocalDate birthDate = LocalDate.parse(dob);
                            LocalDate currentDate = LocalDate.now();
                            Period age = Period.between(birthDate, currentDate);
                            textViewAge.setText(String.valueOf(age.getYears()));
                        } else {
                            textViewAge.setText("N/A");
                        }
                    } else {
                        // Handle case when no data is returned
                        textViewFirstName.setText("N/A");
                        textViewAge.setText("N/A");
                        textViewGender.setText("N/A");
                        textViewCollegeName.setText("N/A");
                        textViewFieldOfStudy.setText("N/A");
                        textViewDept.setText("N/A");
                        textViewGraduationYear.setText("N/A");
                        textViewMobileNumber.setText(mobileNumber);
                        textViewEmail.setText("N/A");
                        textViewStudentId.setText("N/A");
                    }
                } catch (Exception e) {
                    Log.e("FetchDetailsTask", "Error parsing JSON", e);
                }
            } else {
                // Handle error case
                textViewFirstName.setText("Error");
                textViewAge.setText("Error");
                textViewGender.setText("Error");
                textViewCollegeName.setText("Error");
                textViewFieldOfStudy.setText("Error");
                textViewDept.setText("Error");
                textViewGraduationYear.setText("Error");
                textViewMobileNumber.setText(mobileNumber);
                textViewEmail.setText("Error");
                textViewStudentId.setText("Error");
            }
        }
    }
}
