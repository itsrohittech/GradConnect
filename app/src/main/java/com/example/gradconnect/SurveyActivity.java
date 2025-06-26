package com.example.gradconnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

public class SurveyActivity extends AppCompatActivity {

    private String mobileNumber;
    private String status;

    private TextView textViewStatus;
    private TextView question1, question2, question3, question4, question5;
    private EditText answer1, answer2, answer3, answer4, answer5;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);

        // Receive the intent
        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobile_number");

        // Determine the status by checking which extra is passed
        if (intent.hasExtra("startup")) {
            status = "startup";
        } else if (intent.hasExtra("marital")) {
            status = "marital";
        } else if (intent.hasExtra("business")) {
            status = "business";
        } else if (intent.hasExtra("higher_studies")) {
            status = "higher_studies";
        } else if (intent.hasExtra("others")) {
            status = "others";
        }

        Log.d("SurveyActivity", "Received mobile number: " + mobileNumber);
        Log.d("SurveyActivity", "Received status: " + status);

        // Initialize UI elements
        textViewStatus = findViewById(R.id.textView58);
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        question3 = findViewById(R.id.question3);
        question4 = findViewById(R.id.question4);
        question5 = findViewById(R.id.question5);

        answer1 = findViewById(R.id.editTextText9);
        answer2 = findViewById(R.id.editTextText10);
        answer3 = findViewById(R.id.editTextText11);
        answer4 = findViewById(R.id.editTextText12);
        answer5 = findViewById(R.id.editTextText13);

        submitButton = findViewById(R.id.button6);

        // Set the status text
        textViewStatus.setText(status);

        // Set the questions based on the status
        setQuestionsBasedOnStatus(status);

        // Fetch data if necessary
        if (mobileNumber != null && status != null) {
            fetchSurveyData(mobileNumber, status);
        }

        // Handle submit button click to send answers
        submitButton.setOnClickListener(v -> {
                    sendSurveyData();
                    finish();
                });


        // Handle back button
        ImageButton buttonBack = findViewById(R.id.imageButton);
        buttonBack.setOnClickListener(v -> finish());
    }

    // Method to set the questions dynamically based on the status
    private void setQuestionsBasedOnStatus(String status) {
        switch (status) {
            case "startup":
                question1.setText("Are you currently involved in a startup or entrepreneurial venture?");
                question2.setText("What is the nature of your startup?");
                question3.setText("What stage is your startup currently in?");
                question4.setText("Have you secured any funding or investment for your startup?");
                question5.setText("What are the biggest challenges you are facing as a startup founder?");
                break;
            case "marital":
                question1.setText("What is your current marital status?");
                question2.setText("If married, how many years have you been married?");
                question3.setText("Do you have children?");
                question4.setText("How has your marital status influenced your career or further studies?");
                question5.setText("Are you planning any significant changes in your marital status in the near future?");
                break;
            case "business":
                question1.setText("Are you currently employed in a business role?");
                question2.setText("What industry are you working in?");
                question3.setText("What is your current job title or role?");
                question4.setText("What skills or qualifications have been most beneficial in your business role?");
                question5.setText("Are you considering starting your own business in the future?");
                break;
            case "higher_studies":
                question1.setText("Are you currently pursuing or planning to pursue higher studies?");
                question2.setText("What level of higher education are you aiming for?");
                question3.setText("What field or specialization are you studying or interested in?");
                question4.setText("Have you received any scholarships or financial aid for your higher studies?");
                question5.setText("What motivated you to pursue further education after graduation?");
                break;
            case "others":
                question1.setText("Are you engaged in any activities not covered by the previous categories?");
                question2.setText("Please describe any other significant endeavors or interests you are pursuing.");
                question3.setText("How much time do you dedicate to these other activities?");
                question4.setText("What skills or experiences are you gaining from these activities?");
                question5.setText("Are there any achievements in this area that you would like to share?");
                break;
        }
    }

    // Method to fetch survey data from the server
    private void fetchSurveyData(String mobileNumber, String status) {
        new FetchSurveyDataTask().execute(mobileNumber, status);
    }

    // AsyncTask to fetch data from the server
    private class FetchSurveyDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String status = params[1];
            String urlString = IPv4Connection.getBaseUrl() + "survey_get.php?mobile_number=" + mobileNumber + "&options=" + status;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Parse JSON data and set the answers
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("answer_1")) {
                        answer1.setText(jsonObject.optString("answer_1", ""));
                        answer2.setText(jsonObject.optString("answer_2", ""));
                        answer3.setText(jsonObject.optString("answer_3", ""));
                        answer4.setText(jsonObject.optString("answer_4", ""));
                        answer5.setText(jsonObject.optString("answer_5", ""));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to send or update survey data to the server
    private void sendSurveyData() {
        // Ensure all fields are filled
        if (mobileNumber == null || status == null ||
                answer1.getText().toString().isEmpty() ||
                answer2.getText().toString().isEmpty() ||
                answer3.getText().toString().isEmpty() ||
                answer4.getText().toString().isEmpty() ||
                answer5.getText().toString().isEmpty()) {
            Log.e("SurveyActivity", "One or more fields are empty.");
            return;
        }

        Log.d("SurveyActivity", "Preparing to send or update survey data.");

        new SendSurveyDataTask().execute(
                mobileNumber,
                status,
                question1.getText().toString(),
                answer1.getText().toString(),
                question2.getText().toString(),
                answer2.getText().toString(),
                question3.getText().toString(),
                answer3.getText().toString(),
                question4.getText().toString(),
                answer4.getText().toString(),
                question5.getText().toString(),
                answer5.getText().toString()
        );
    }

    private class SendSurveyDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String status = params[1];
            String question_1 = params[2];
            String answer_1 = params[3];
            String question_2 = params[4];
            String answer_2 = params[5];
            String question_3 = params[6];
            String answer_3 = params[7];
            String question_4 = params[8];
            String answer_4 = params[9];
            String question_5 = params[10];
            String answer_5 = params[11];

            HttpURLConnection conn = null;
            try {
                // Construct the URL for the PHP script
                URL url = new URL(IPv4Connection.getBaseUrl() + "survey_post.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // Use POST method for sending data
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Prepare POST data with URL encoding
                String postData = "mobile_number=" + URLEncoder.encode(mobileNumber, "UTF-8") +
                        "&options=" + URLEncoder.encode(status, "UTF-8") +
                        "&question_1=" + URLEncoder.encode(question_1, "UTF-8") +
                        "&answer_1=" + URLEncoder.encode(answer_1, "UTF-8") +
                        "&question_2=" + URLEncoder.encode(question_2, "UTF-8") +
                        "&answer_2=" + URLEncoder.encode(answer_2, "UTF-8") +
                        "&question_3=" + URLEncoder.encode(question_3, "UTF-8") +
                        "&answer_3=" + URLEncoder.encode(answer_3, "UTF-8") +
                        "&question_4=" + URLEncoder.encode(question_4, "UTF-8") +
                        "&answer_4=" + URLEncoder.encode(answer_4, "UTF-8") +
                        "&question_5=" + URLEncoder.encode(question_5, "UTF-8") +
                        "&answer_5=" + URLEncoder.encode(answer_5, "UTF-8");

                Log.d("SendSurveyDataTask", "Sending POST data: " + postData);

                // Write data to output stream
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.flush();
                writer.close();

                // Check HTTP response code
                int responseCode = conn.getResponseCode();
                Log.d("SendSurveyDataTask", "Response code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response from input stream
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "Error: HTTP response code " + responseCode;
                }
            } catch (Exception e) {
                Log.e("SendSurveyDataTask", "Error: " + e.getMessage(), e);
                return "Error: " + e.getMessage();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("SendSurveyDataTask", "Response: " + result);
            } else {
                Log.e("SendSurveyDataTask", "Failed to send or update survey data.");
            }
        }
    }
}

