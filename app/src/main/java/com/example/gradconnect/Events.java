package com.example.gradconnect;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Events extends Fragment {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    public Events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // Initialize UI elements
        menuButton = view.findViewById(R.id.imageButton);
        recyclerView = view.findViewById(R.id.recycler);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        // Fetch events data
        new FetchEventsTask().execute(IPv4Connection.getBaseUrl()+"events_get.php");

        // Access DrawerLayout and NavigationView from the hosting activity
        if (getActivity() instanceof HomeStudentsActivity) {
            HomeStudentsActivity mainActivity = (HomeStudentsActivity) getActivity();
            drawerLayout = mainActivity.getDrawerLayout();
            navigationView = mainActivity.getNavigationView();
        }

        // Set up the ImageButton to open the drawer
        if (drawerLayout != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(navigationView);
                }
            });
        }

        return view;
    }

    private class FetchEventsTask extends AsyncTask<String, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(String... urls) {
            String urlString = urls[0];
            List<Event> events = new ArrayList<>();

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String eventTitle = jsonObject.getString("event_title");
                    String date = jsonObject.getString("date");
                    String eventContent = jsonObject.getString("event_content");
                    events.add(new Event(eventTitle, date, eventContent));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return events;
        }

        @Override
        protected void onPostExecute(List<Event> result) {
            if (result != null) {
                eventList.clear();
                eventList.addAll(result);
                eventAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
