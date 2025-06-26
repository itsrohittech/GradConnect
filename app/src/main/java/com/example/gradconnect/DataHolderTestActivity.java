package com.example.gradconnect;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DataHolderTestActivity extends AppCompatActivity {

    private static final String TAG = "DataHolderTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rough);

        // Get DataHolder instance
        DataHolder dataHolder = DataHolder.getInstance();

        // Log the default values
        Log.d(TAG, "First Name: " + dataHolder.getFirstName()); // Should be null
        Log.d(TAG, "DOB: " + dataHolder.getDob()); // Should be null
        Log.d(TAG, "Gender: " + dataHolder.getGender()); // Should be null
        Log.d(TAG, "College Name: " + dataHolder.getCollegeName()); // Should be null
        Log.d(TAG, "Field of Study: " + dataHolder.getFieldOfStudy()); // Should be null
        Log.d(TAG, "Dept: " + dataHolder.getDept()); // Should be null
        Log.d(TAG, "Graduation Year: " + dataHolder.getGraduationYear()); // Should be null
        Log.d(TAG, "Mobile Number: " + dataHolder.getMobileNumber()); // Should be null
        Log.d(TAG, "Email: " + dataHolder.getEmail()); // Should be null
        Log.d(TAG, "Student ID: " + dataHolder.getStudentId()); // Should be null
    }
}

