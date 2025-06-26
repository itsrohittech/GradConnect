package com.example.gradconnect;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Notices extends Fragment {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;
    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList = new ArrayList<>();

    public Notices() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notices, container, false);

        // Initialize UI elements
        menuButton = view.findViewById(R.id.imageButton);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeAdapter = new NoticeAdapter(noticeList);
        recyclerView.setAdapter(noticeAdapter);

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

        // Fetch data from server
        new FetchNoticesTask().execute(IPv4Connection.getBaseUrl() + "notices_get.php");

        return view;
    }

    private class FetchNoticesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    jsonResponse = stringBuilder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null || result.isEmpty()) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Parse the result as JSONArray
                JSONArray jsonArray = new JSONArray(result);
                noticeList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject noticeObject = jsonArray.getJSONObject(i);
                    String title = noticeObject.optString("notice_title", "No Title");
                    String content = noticeObject.optString("notice_content", "No Content");
                    String date = noticeObject.optString("notice_date", "No Date");
                    Notice notice = new Notice(title, content, date);
                    noticeList.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to parse data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
