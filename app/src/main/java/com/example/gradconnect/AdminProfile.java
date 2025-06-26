package com.example.gradconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class AdminProfile extends Fragment {

    private static final String ARG_MOBILE_NUMBER = "mobile_number";
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mobileNumber;
    private String encodedImage;

    private TextView textView23;
    private TextView textView24;
    private EditText editTextText7;
    private EditText editTextText8;
    private EditText editTextEmail;
    private EditText editTextMobile;
    private EditText editTextConfirmPassword;
    private Button updateButton;
    private ImageButton openDrawerButton;
    private ImageButton imageButtonSelectImage;
    private ImageView profileImageView;
    private Uri imageUri;

    public AdminProfile() {
        // Required empty public constructor
    }

    public static AdminProfile newInstance(String mobileNumber) {
        AdminProfile fragment = new AdminProfile();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        // Initialize UI elements
        textView23 = view.findViewById(R.id.textView23);
        textView24 = view.findViewById(R.id.textView24);
        editTextText7 = view.findViewById(R.id.editTextText7);
        editTextText8 = view.findViewById(R.id.editTextText8);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        updateButton = view.findViewById(R.id.button4);
        openDrawerButton = view.findViewById(R.id.imageButton);
        imageButtonSelectImage = view.findViewById(R.id.imageButton3);
        profileImageView = view.findViewById(R.id.imageButton3); // Ensure this ID matches your layout

        // Fetch user data
        fetchUserData();

        // Set up the update button click listener
        updateButton.setOnClickListener(v -> updateUserData());

        // Open gallery to select image
        imageButtonSelectImage.setOnClickListener(v -> openGallery());

        // Open the drawer
        openDrawerButton.setOnClickListener(v -> {
            HomeAdminActivity drawerController = (HomeAdminActivity) getActivity();
            if (drawerController != null) {
                drawerController.openDrawer();
            }
        });

        return view;
    }

    private void fetchUserData() {
        new FetchUserDataTask().execute(mobileNumber);
    }

    private class FetchUserDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String urlString = IPv4Connection.getBaseUrl() + "admin_profile_get.php?username=" + mobileNumber;

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
            Log.d("AdminProfileFragment", "Response: " + result); // Log the response for debugging

            if (result != null) {
                try {
                    // Parse JSON data
                    JSONObject jsonObject = new JSONObject(result);

                    // Check for "data" object
                    if (jsonObject.has("data")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        textView23.setText(dataObject.getString("name"));
                        textView24.setText(dataObject.getString("mobile_number"));
                        editTextText7.setText(dataObject.getString("name"));
                        editTextText8.setText(dataObject.getString("college_name"));
                        editTextEmail.setText(dataObject.getString("email"));
                        editTextMobile.setText(dataObject.getString("mobile_number"));
                        editTextConfirmPassword.setText(dataObject.getString("password"));

                        // Set the profile image if URL is available
                        String imageUrl = dataObject.optString("image_url", null);
                        if (imageUrl != null) {
                            Glide.with(getActivity())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.baseline_person_24)
                                    .into(profileImageView);
                        }
                    } else {
                        Toast.makeText(getActivity(), "No data found for this mobile number", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to fetch details", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void updateUserData() {
        // Check if image is selected, if not set to empty string
        String imageBase64 = (encodedImage != null) ? encodedImage : "";

        new UpdateUserDataTask().execute(
                editTextText7.getText().toString(),    // Name
                editTextText8.getText().toString(),    // College
                editTextEmail.getText().toString(),    // Email
                editTextConfirmPassword.getText().toString(), // Password
                editTextMobile.getText().toString(),    // Mobile Number
                imageBase64 // Pass the Base64-encoded image string
        );
    }


    private class UpdateUserDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String college = params[1];
            String email = params[2];
            String password = params[3];
            String mobileNumber = params[4];
            String imageBase64 = params[5];

            // Log the values
            Log.d("UpdateUserDataTask", "Name: " + name);
            Log.d("UpdateUserDataTask", "College: " + college);
            Log.d("UpdateUserDataTask", "Email: " + email);
            Log.d("UpdateUserDataTask", "Password: " + password);
            Log.d("UpdateUserDataTask", "Mobile Number: " + mobileNumber);
            Log.d("UpdateUserDataTask", "Image Base64: " + imageBase64);

            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(IPv4Connection.getBaseUrl() + "admin_profile_update.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Prepare POST data
                String postData = "name=" + URLEncoder.encode(name, "UTF-8") +
                        "&college_name=" + URLEncoder.encode(college, "UTF-8") +
                        "&email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&mobile_number=" + URLEncoder.encode(mobileNumber, "UTF-8") +
                        "&image_url=" + URLEncoder.encode(imageBase64, "UTF-8");

                Log.d("UpdateUserDataTask", "POST Data: " + postData);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.flush();
                writer.close();

                InputStream inputStream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        Log.e("UpdateUserDataTask", "Exception while closing reader: " + e.getMessage(), e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("UpdateUserDataTask", "Raw Response: " + result);

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if ("success".equals(status)) {
                        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error parsing update response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImageView.setImageBitmap(bitmap);

                // Convert image to Base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
