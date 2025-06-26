package com.example.gradconnect;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class Home extends Fragment {

    private static final String ARG_MOBILE_NUMBER = "mobile_number";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String mobileNumber;

    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String mobileNumber) {
        Home fragment = new Home();
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

    // Factory method to create a new instance of this fragment
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI elements
        ImageButton button = view.findViewById(R.id.imageButton4);
        ImageButton imageButton5 = view.findViewById(R.id.imageButton5);
        ImageButton imageButton6 = view.findViewById(R.id.imageButton6);
        ImageButton imageButton7 = view.findViewById(R.id.imageButton7);

        ImageButton menuButton = view.findViewById(R.id.imageButton);
        menuButton.setOnClickListener(v -> drawerLayout.open());

        // Set up button click listeners
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AlumniRegistrationActivity.class);
            startActivity(intent);
        });

        imageButton5.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AlumniVerificationActivity.class);
            intent.putExtra("mobile_number",mobileNumber);
            startActivity(intent);
        });

        imageButton6.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        imageButton7.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrackingStatusActivity.class);
            intent.putExtra("mobile_number",mobileNumber);
            startActivity(intent);
        });
        ImageButton imageButton2 = view.findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity that hosts the RecycleCenterFragment
                Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });


        // Access DrawerLayout and NavigationView
        if (getActivity() instanceof HomeStudentsActivity) {
            HomeStudentsActivity mainActivity = (HomeStudentsActivity) getActivity();
            drawerLayout = mainActivity.getDrawerLayout();
            navigationView = mainActivity.getNavigationView();
        }

        return view;
    }
}
