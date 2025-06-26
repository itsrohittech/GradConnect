package com.example.gradconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeStudentsActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentAdapter;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_students);

        // Get data passed to the activity (if any)
        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobile_number"); // Example of receiving mobileNumber

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up the ViewPager2 and FragmentStateAdapter
        viewPager = findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false);
        fragmentAdapter = new ScreenSlidePagerAdapter(this, mobileNumber); // Pass mobileNumber to adapter
        viewPager.setAdapter(fragmentAdapter);

        // Set up the TabLayout
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
                startActivity(new Intent(HomeStudentsActivity.this, NotificationsActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
                return true;
            } else if (id == R.id.logout) {
                startActivity(new Intent(HomeStudentsActivity.this, LoginActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection
                return true;
            }

            return false;
        });
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

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
                    fragment = new Home();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 1:
                    fragment = new Events();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 2:
                    fragment = new Notices();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                case 3:
                    fragment = new Profile();
                    Log.d("LoginActivity", "Sending mobile_number: " + mobileNumber);
                    break;
                default:
                    fragment = new Home();
            }

            fragment.setArguments(bundle); // Attach bundle to the fragment
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 4; // Number of tabs
        }
    }

    // Optional: Add methods to open/close drawer if needed
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
