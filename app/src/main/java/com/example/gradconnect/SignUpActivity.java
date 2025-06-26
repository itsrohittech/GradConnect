package com.example.gradconnect;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, mobileEditText, usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);

        // Show the dialog when the activity starts
        showRoleDialog();

        // Handle system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        nameEditText = findViewById(R.id.editTextText2);
        emailEditText = findViewById(R.id.editTextText6);
        mobileEditText = findViewById(R.id.editText2);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextTextPassword2);

        // Set up the sign-up button
        Button signUpButton = findViewById(R.id.button3);
        signUpButton.setOnClickListener(v -> {
            // Get the input data
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String mobile = mobileEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (validateInput(name, email, mobile, username, password)) {
                // Start AsyncTask to send data
                new SignUpTask().execute(name, email, mobile, username, password);
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show the dialog when the activity starts
    private void showRoleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Registration Information")
                .setMessage("If you're a student, no need to enter a Username.\nIf you're a College Admin, please enter a Username.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Input validation function
    private boolean validateInput(String name, String email, String mobile, String username, String password) {
        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (!isStudent() && username.isEmpty()) {
            return false;
        }
        return true;
    }

    // Assume students don't require a username
    private boolean isStudent() {
        return usernameEditText.getText().toString().trim().isEmpty();
    }

    // AsyncTask to send registration data to the PHP script
    private class SignUpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length < 5) {
                return "Error: Not enough parameters";
            }

            String result = "";
            String name = params[0];
            String email = params[1];
            String mobile = params[2];
            String username = params[3];
            String password = params[4];

            try {
                URL url = new URL(IPv4Connection.getBaseUrl() + "signup.php"); // PHP script URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Prepare POST data
                String postData = "name=" + name + "&email=" + email + "&mobile_number=" + mobile
                        + "&username=" + username + "&password=" + password;

                // Send POST data
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    result = response.toString();
                } else {
                    result = "Error: HTTP " + responseCode;
                }

            } catch (Exception e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Handle the JSON response
                if (result.startsWith("{")) {
                    JSONObject jsonResponse = new JSONObject(result);
                    String status = jsonResponse.optString("status", "error");

                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                        finish(); // Close activity after successful sign-up
                    } else {
                        String errorMessage = jsonResponse.optString("message", "Unknown error");
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("SignUpActivity", "Invalid JSON response: " + result);
                    Toast.makeText(SignUpActivity.this, "Server returned invalid response", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("SignUpActivity", "JSON parsing error: " + e.getMessage());
                Toast.makeText(SignUpActivity.this, "Failed to parse server response", Toast.LENGTH_LONG).show();
            }
        }
    }
}
