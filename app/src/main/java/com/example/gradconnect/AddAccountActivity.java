package com.example.gradconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddAccountActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextMobileNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);

        // Initialize EditText fields
        editTextName = findViewById(R.id.editTextText7);
        editTextMobileNumber = findViewById(R.id.editTextText8);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Set up the ImageButton to finish the activity
        ImageButton finishButton = findViewById(R.id.imageButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });

        // Set up the button to submit the form
        Button submitButton = findViewById(R.id.button4);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount(); // Call method to handle form submission
            }
        });
    }

    private void addAccount() {
        // Retrieve the data from EditText fields
        final String name = editTextName.getText().toString().trim();
        final String mobileNumber = editTextMobileNumber.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        // Validate the input
        if (name.isEmpty() || mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AddAccountActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define the URL of your PHP script
        String url = IPv4Connection.getBaseUrl() + "add_accounts.php"; // Ensure this URL is correct

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest for the POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AddAccountActivity", "Response: " + response); // Log the response
                        // Handle the response from the server
                        Toast.makeText(AddAccountActivity.this, "Successfully Added", Toast.LENGTH_LONG).show();
                        finish(); // Close the activity only after getting a response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AddAccountActivity", "Error: " + error.getMessage()); // Log the error
                // Handle error
                Toast.makeText(AddAccountActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Pass the parameters to the PHP script
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("mobile_number", mobileNumber);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }

}
