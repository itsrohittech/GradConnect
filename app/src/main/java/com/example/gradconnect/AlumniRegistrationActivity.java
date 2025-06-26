package com.example.gradconnect;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AlumniRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etDob, etGender, etMobileNumber, etEmail, etStudentId, etCollegeName, etFieldOfStudy, etGraduationYear, etDept, etAddress, etCity, etState, etCountry;
    private Calendar calendar;
    private static final String REGISTER_URL = IPv4Connection.getBaseUrl() + "alumni_details_post.php"; // Replace with your server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.alumni_registration);

        // Initialize EditText fields
        etFirstName = findViewById(R.id.editTextFirstName);
        etLastName = findViewById(R.id.editTextLastName);
        etDob = findViewById(R.id.editTextDOB);
        etGender = findViewById(R.id.editTextGender);
        etMobileNumber = findViewById(R.id.editTextContact);
        etEmail = findViewById(R.id.editTextEmail);
        etStudentId = findViewById(R.id.editTextStudentID);
        etCollegeName = findViewById(R.id.editTextCollegeName);
        etFieldOfStudy = findViewById(R.id.editTextMajorField);
        etGraduationYear = findViewById(R.id.editTextGradYear);
        etDept = findViewById(R.id.editTextDepartment);
        etAddress = findViewById(R.id.editTextCurrentAddress);
        etCity = findViewById(R.id.editTextCity);
        etState = findViewById(R.id.editTextState);
        etCountry = findViewById(R.id.editTextCountry);

        // Setup date picker for etDob
        calendar = Calendar.getInstance();
        etDob.setOnClickListener(v -> showDatePickerDialog());

        // Setup the back button
        ImageButton buttonBack = findViewById(R.id.imageButton);
        buttonBack.setOnClickListener(v -> finish());

        // Setup the submit button
        Button buttonSubmit = findViewById(R.id.registerButton);
        buttonSubmit.setOnClickListener(v -> {
            submitRegistration(); // Submit registration form
            finish(); // Close the activity after submission
        });
    }

    // Show DatePickerDialog when DOB field is clicked
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            // Set the selected date in the calendar instance
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Format the date to YYYY/MM/DD and set it to the EditText
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            etDob.setText(sdf.format(calendar.getTime()));
        };

        // Show DatePickerDialog
        new DatePickerDialog(AlumniRegistrationActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submitRegistration() {
        // Get user input
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String collegeName = etCollegeName.getText().toString().trim();
        String fieldOfStudy = etFieldOfStudy.getText().toString().trim();
        String graduationYear = etGraduationYear.getText().toString().trim();
        String dept = etDept.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        // Validate input (basic example)
        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create POST data
        Map<String, String> postData = new HashMap<>();
        postData.put("first_name", firstName);
        postData.put("last_name", lastName);
        postData.put("dob", dob);  // Already formatted as YYYY/MM/DD
        postData.put("gender", gender);
        postData.put("mobile_number", mobileNumber);
        postData.put("email", email);
        postData.put("student_id", studentId);
        postData.put("college_name", collegeName);
        postData.put("field_of_study", fieldOfStudy);
        postData.put("graduation_year", graduationYear);
        postData.put("dept", dept);
        postData.put("address", address);
        postData.put("city", city);
        postData.put("state", state);
        postData.put("country", country);

        // Execute the background task
        new SubmitRegistrationTask(this).execute(postData);
    }

    private static class SubmitRegistrationTask extends AsyncTask<Map<String, String>, Void, String> {

        private Context context;
        private static final String REGISTER_URL = IPv4Connection.getBaseUrl() + "alumni_details_post.php"; // Replace with your server URL

        public SubmitRegistrationTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Map<String, String>... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(REGISTER_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Build the POST data string
                StringBuilder postDataString = new StringBuilder();
                Map<String, String> postData = params[0];
                for (Map.Entry<String, String> entry : postData.entrySet()) {
                    if (postDataString.length() != 0) {
                        postDataString.append('&');
                    }
                    postDataString.append(entry.getKey()).append('=').append(entry.getValue());
                }

                // Write the POST data to the output stream
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(postDataString.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                // Check response code
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Success: Data sent successfully.";
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Use the passed context for the Toast
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}
