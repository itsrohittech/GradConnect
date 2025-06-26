package com.example.gradconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrackingStatusActivity extends AppCompatActivity {

    private String mobileNumber;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tracking_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mobileNumber = getIntent().getStringExtra("mobile_number");

        ImageButton button = findViewById(R.id.imageButton8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackingStatusActivity.this, SurveyActivity.class);
                intent.putExtra("mobile_number",mobileNumber);
                intent.putExtra("startup",status);
                Log.d("SurveyActivity", "Received status: " + mobileNumber);
                Log.d("SurveyActivity", "Received status: " + status);
                startActivity(intent);
            }
        });
        ImageButton imageButton9 = findViewById(R.id.imageButton9);
        imageButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackingStatusActivity.this, SurveyActivity.class);
                intent.putExtra("mobile_number",mobileNumber);
                intent.putExtra("marital",status);
                Log.d("SurveyActivity", "Received status: " + mobileNumber);
                Log.d("SurveyActivity", "Received status: " + status);
                startActivity(intent);
            }
        });
        ImageButton imageButton10 = findViewById(R.id.imageButton10);
        imageButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackingStatusActivity.this, SurveyActivity.class);
                intent.putExtra("mobile_number",mobileNumber);
                intent.putExtra("business",status);
                Log.d("SurveyActivity", "Received status: " + mobileNumber);
                Log.d("SurveyActivity", "Received status: " + status);
                startActivity(intent);
            }
        });
        ImageButton imageButton11 = findViewById(R.id.imageButton11);
        imageButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackingStatusActivity.this, SurveyActivity.class);
                intent.putExtra("mobile_number",mobileNumber);
                intent.putExtra("higher_studies",status);
                Log.d("SurveyActivity", "Received status: " + mobileNumber);
                Log.d("SurveyActivity", "Received status: " + status);
                startActivity(intent);
            }
        });
        ImageButton imageButton12 = findViewById(R.id.imageButton12);
        imageButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackingStatusActivity.this, SurveyActivity.class);
                intent.putExtra("mobile_number",mobileNumber);
                intent.putExtra("others",status);
                Log.d("SurveyActivity", "Received status: " + mobileNumber);
                Log.d("SurveyActivity", "Received status: " + status);
                startActivity(intent);
            }
        });
        ImageButton buttonBack = findViewById(R.id.imageButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity
                finish();
            }
        });

    }
}