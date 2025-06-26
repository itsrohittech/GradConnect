package com.example.gradconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CollegeProfile extends Fragment {

    private static final String ARG_COLLEGE_ID = "mobile_number";
    private static final int PICK_IMAGE_REQUEST = 1;
    private String collegeId;
    private Uri imageUri;

    // UI elements
    private TextView textViewCollegeName;
    private EditText editTextCollegeName;
    private EditText editTextEmail;
    private EditText editTextMobile;
    private EditText editTextConfirmPassword;
    private ImageButton imageButtonSelectImage;
    private ImageView profileImageView;
    private Button updateButton;

    public CollegeProfile() {
        // Required empty public constructor
    }

    public static CollegeProfile newInstance(String collegeId) {
        CollegeProfile fragment = new CollegeProfile();
        Bundle args = new Bundle();
        args.putString(ARG_COLLEGE_ID, collegeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            collegeId = getArguments().getString(ARG_COLLEGE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_college_profile, container, false);

        // Initialize UI elements
        textViewCollegeName = view.findViewById(R.id.textView23);
        editTextCollegeName = view.findViewById(R.id.editTextText8); // Updated ID for college_name
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile); // Updated ID for mobile_number
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword); // Updated ID for password
        updateButton = view.findViewById(R.id.button4);
        imageButtonSelectImage = view.findViewById(R.id.imageButton3); // Button to open gallery
        profileImageView = view.findViewById(R.id.imageButton3); // Correct ID for ImageView

        // Fetch college data
        fetchCollegeData();

        // Set up the update button click listener
        updateButton.setOnClickListener(v -> updateCollegeData());

        // Initialize ImageButton for menu
        ImageButton menuButton = view.findViewById(R.id.imageButton);
        menuButton.setOnClickListener(v -> {
            if (getActivity() instanceof HomeCollegeActivity) {
                ((HomeCollegeActivity) getActivity()).openDrawer();
            }
        });

        // Open gallery to select image
        imageButtonSelectImage.setOnClickListener(v -> openGallery());

        return view;
    }

    private void fetchCollegeData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = IPv4Connection.getBaseUrl() + "college_profile_get.php?username=" + collegeId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject collegeData = response.getJSONObject("data");
                                textViewCollegeName.setText(collegeData.getString("college_name"));
                                editTextCollegeName.setText(collegeData.getString("college_name"));// Updated to location
                                editTextEmail.setText(collegeData.getString("email"));
                                editTextMobile.setText(collegeData.getString("mobile_number"));
                                editTextConfirmPassword.setText(collegeData.getString("password"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateCollegeData() {
        String url = IPv4Connection.getBaseUrl() + "college_profile_update.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response from the server
                    if (response.contains("Update successful")) {
                        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Show toast for failure
                        Toast.makeText(getContext(), "Profile update failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("college_id", collegeId); // Assuming `collegeId` is the identifier
                params.put("college_name", editTextCollegeName.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("mobile_number", editTextMobile.getText().toString());
                params.put("password", editTextConfirmPassword.getText().toString());
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
