package com.example.gradconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bumptech.glide.Glide;
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

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends Fragment {

    private static final String ARG_MOBILE_NUMBER = "mobile_number";
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mobileNumber;
    private EditText nameEditText, collegeEditText, emailEditText, mobileEditText, passwordEditText;
    private TextView nameTextView, mobileTextView;
    private Button updateButton;
    private ImageButton imageButton3; // Button to open gallery
    private ImageView profileImageView; // ImageView to display selected image
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Uri imageUri; // URI of the selected image
    private String encodedImage;
    // Encoded image as Base64 string to send to server

    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String mobileNumber) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        nameEditText = view.findViewById(R.id.editTextText7);
        collegeEditText = view.findViewById(R.id.editTextText8);
        emailEditText = view.findViewById(R.id.editTextEmail);
        mobileEditText = view.findViewById(R.id.editTextMobileNumber);
        passwordEditText = view.findViewById(R.id.editTextConfirmPassword);
        updateButton = view.findViewById(R.id.button4);
        nameTextView = view.findViewById(R.id.textView23);
        mobileTextView = view.findViewById(R.id.textView24);
        imageButton3 = view.findViewById(R.id.imageButton3);
        profileImageView = view.findViewById(R.id.imageButton3); // Corrected to profileImageView

        ImageButton menuButton = view.findViewById(R.id.imageButton);
        menuButton.setOnClickListener(v -> drawerLayout.open());

        // Fetch user details from the server
        if (mobileNumber != null) {
            fetchUserDetails(mobileNumber);
        }

        // Update button listener
        updateButton.setOnClickListener(v -> {
            // Update user details with the image URL
            updateUserDetails(
                    nameEditText.getText().toString(),
                    collegeEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    encodedImage // Pass the Base64-encoded image string
            );
        });

        // Open gallery to select image
        imageButton3.setOnClickListener(v -> openGallery());

        if (getActivity() instanceof HomeStudentsActivity) {
            HomeStudentsActivity mainActivity = (HomeStudentsActivity) getActivity();
            drawerLayout = mainActivity.getDrawerLayout();
            navigationView = mainActivity.getNavigationView();
        }

        return view;
    }

    private void fetchUserDetails(String mobileNumber) {
        new FetchUserDetailsTask().execute(mobileNumber);
    }

    private class FetchUserDetailsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String urlString = IPv4Connection.getBaseUrl() + "profile_get.php?mobile_number=" + mobileNumber;

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
            Log.d("ProfileFragment", "Response: " + result); // Log the response for debugging

            if (result != null) {
                try {
                    // Parse JSON data
                    JSONObject jsonObject = new JSONObject(result);

                    // Check for "data" object
                    if (jsonObject.has("data")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        nameEditText.setText(dataObject.getString("name"));
                        collegeEditText.setText(dataObject.getString("college_name"));
                        emailEditText.setText(dataObject.getString("email"));
                        mobileEditText.setText(dataObject.getString("mobile_number"));
                        passwordEditText.setText(dataObject.getString("password"));
                        nameTextView.setText(dataObject.getString("name"));
                        mobileTextView.setText(dataObject.getString("mobile_number"));

                        // Set the profile image if URL is available
                        String imageUrl = dataObject.optString("image_url", null);
                        if (imageUrl != null) {
                            Glide.with(getActivity())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.baseline_person_24) // Optional placeholder
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

    private void updateUserDetails(String name, String college, String email, String password, String imageBase64) {
        new UpdateUserDetailsTask().execute(name, college, email, password, mobileNumber, imageBase64);
    }

    private class UpdateUserDetailsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String college = params[1];
            String email = params[2];
            String password = params[3];
            String mobileNumber = params[4];
            String imageBase64 = params[5];

            try {
                URL url = new URL(IPv4Connection.getBaseUrl() + "profile_update.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Prepare POST data
                String postData = "name=" + name + "&college_name=" + college + "&email=" + email + "&password=" + password + "&mobile_number=" + mobileNumber + "&image_url=" + imageBase64;

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.flush();
                writer.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // **Debugging**: Print the raw server response to log before parsing
                Log.d("Server Response", response.toString());

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
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if ("success".equals(status)) {
                        Toast.makeText(getActivity(), "Details updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to update details", Toast.LENGTH_SHORT).show();
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
            profileImageView.setImageURI(imageUri); // Display selected image

            // Encode image to Base64
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                encodedImage = encodeImageToBase64(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
