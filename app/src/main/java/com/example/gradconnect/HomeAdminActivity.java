package com.example.gradconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeAdminActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_admin);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobile_number");

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up ViewPager2 and FragmentStateAdapter
        viewPager = findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false); // Disable user input for swiping
        fragmentAdapter = new ScreenSlidePagerAdapter(this,mobileNumber);
        viewPager.setAdapter(fragmentAdapter);

        // Set up TabLayout with ViewPager2
        tabLayout = findViewById(R.id.TabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            tab.setIcon(R.drawable.baseline_home_24);
                            break;
                        case 1:
                            tab.setText("Events");
                            tab.setIcon(R.drawable.baseline_event_24);
                            break;
                        case 2:
                            tab.setText("Notices");
                            tab.setIcon(R.drawable.baseline_event_note_24);
                            break;
                        case 3:
                            tab.setText("Profile");
                            tab.setIcon(R.drawable.baseline_person_24);
                            break;
                    }
                }).attach();

        // Set up NavigationView item selection listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.notifications) {
                startActivity(new Intent(HomeAdminActivity.this, CreateNotificationActivity.class));
            } else if (id == R.id.student_details) {
                startActivity(new Intent(HomeAdminActivity.this, StudentDetailsActivity.class));
            } else if (id == R.id.logout) {
                startActivity(new Intent(HomeAdminActivity.this, LoginActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
            return true;
        });
    }

    // Method to open the drawer programmatically
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Adapter class for managing fragments in ViewPager2
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private String mobileNumber;

        public ScreenSlidePagerAdapter(FragmentActivity fa, String mobileNumber) {
            super(fa);
            this.mobileNumber = mobileNumber;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putString("mobile_number", mobileNumber); // Pass mobileNumber to fragments

            switch (position) {
                case 0:
                    fragment = new HomeAdmin();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 1:
                    fragment = new CreateEvents();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 2:
                    fragment = new CreateNotice();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 3:
                    fragment = new AdminProfile();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                default:
                    fragment = new HomeAdmin();
            }

            fragment.setArguments(bundle); // Attach bundle to the fragment
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 4; // Number of tabs
        }
    }
}
