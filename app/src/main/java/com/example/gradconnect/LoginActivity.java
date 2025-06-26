package com.example.gradconnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String mobileNumber;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextTextPassword);


        // Show dialog with login information
        showDialog();

        // Sign up button listener
        TextView textView = findViewById(R.id.textView7);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Login button listener
        Button loginButton = findViewById(R.id.button3);
        loginButton.setOnClickListener(v -> {
            String userInput = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (isValidMobileNumber(userInput)) {
                new LoginTask().execute("mobile_number", userInput, password);
            } else {
                new LoginTask().execute("username", userInput, password);
            }
        });
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Information")
                .setMessage("If you're a student, enter your mobile number.\nIf you're a college admin, enter your username.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // No action needed
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private boolean isValidMobileNumber(String input) {
        Pattern pattern = Pattern.compile("^[0-9]{10}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        private String inputType;

        @Override
        protected String doInBackground(String... params) {
            inputType = params[0];
            String userInput = params[1];
            String password = params[2];
            String result = "";

            try {
                URL url = new URL(IPv4Connection.getBaseUrl() + "login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                String postData = inputType.equals("mobile_number") ?
                        "mobile_number=" + userInput + "&password=" + password :
                        "username=" + userInput + "&password=" + password;

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
                if (result.startsWith("{")) {
                    JSONObject jsonResponse = new JSONObject(result);
                    String status = jsonResponse.optString("status", "error");

                    if (status.equalsIgnoreCase("success")) {
                        String userInput = usernameEditText.getText().toString().trim();
                        if (inputType.equals("mobile_number")) {
                            // Navigate to Student Home
                            Intent intent = new Intent(LoginActivity.this, HomeStudentsActivity.class);
                            intent.putExtra("mobile_number",userInput);
                            Log.d("LoginActivity", "Sending mobile_number: " + userInput);
                            startActivity(intent);
                        } else if (userInput.equalsIgnoreCase("admin")) {
                            // Navigate to Admin Home
                            Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                            intent.putExtra("mobile_number",userInput);
                            Log.d("LoginActivity", "Sending mobile_number: " + userInput);
                            startActivity(intent);
                        } else {
                            // Navigate to College Home
                            Intent intent = new Intent(LoginActivity.this, HomeCollegeActivity.class);
                            intent.putExtra("mobile_number",userInput);
                            Log.d("LoginActivity", "Sending mobile_number: " + userInput);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        String errorMessage = jsonResponse.optString("message", "Unknown error");
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("LoginActivity", "Invalid JSON response: " + result);
                    Toast.makeText(LoginActivity.this, "Server returned invalid response", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("LoginActivity", "JSON parsing error: " + e.getMessage());
                Toast.makeText(LoginActivity.this, "Failed to parse server response", Toast.LENGTH_LONG).show();
            }
        }
    }
}
