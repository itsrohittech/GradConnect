package com.example.gradconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class AlumniVeriAdapter extends RecyclerView.Adapter<AlumniVeriAdapter.AlumniViewHolder> {



    private List<AlumniVeri> alumniList;
    private final String baseUrl = IPv4Connection.getBaseUrl(); // Replace with your server URL

    public AlumniVeriAdapter(List<AlumniVeri> alumniList, VerificationPageActivity verificationPageActivity) {
        this.alumniList = alumniList;
    }

    @NonNull
    @Override
    public AlumniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alumni_verification_recycler, parent, false);
        return new AlumniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumniViewHolder holder, int position) {
        AlumniVeri alumni = alumniList.get(position);
        holder.nameTextView.setText(alumni.getName());
        holder.dobTextView.setText(alumni.getDob());
        holder.genderTextView.setText(alumni.getGender());
        holder.collegeNameTextView.setText(alumni.getCollegeName());
        holder.fieldOfStudyTextView.setText(alumni.getFieldOfStudy());
        holder.deptTextView.setText(alumni.getDept());
        holder.mobileNumberTextView.setText(alumni.getMobileNumber());
        holder.emailTextView.setText(alumni.getEmail());

        holder.deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Get the adapter position
                            int position = holder.getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                // Proceed with deletion
                                deleteAlumni(alumni.getName(), position, holder.itemView.getContext());
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    private void deleteAlumni(String firstName, int position, Context context) {
        // URL for deleting alumni details and login details
        String deleteAlumniUrl = baseUrl + "delete.php?first_name=" + firstName;
        String deleteLoginUrl = baseUrl + "delete_login.php?name=" + firstName;
        Log.d("AlumniVeriAdapter", "Deleting alumni with name: " + firstName);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // First delete from alumni_details
        StringRequest deleteAlumniRequest = new StringRequest(Request.Method.GET, deleteAlumniUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Alumni details deleted successfully")) {
                            // Then delete from login table
                            StringRequest deleteLoginRequest = new StringRequest(Request.Method.GET, deleteLoginUrl,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.trim().equals("Login details deleted successfully")) {
                                                // Remove the item from the list
                                                alumniList.remove(position);
                                                // Notify adapter about item removal
                                                notifyItemRemoved(position);
                                                // Notify adapter of the item range change
                                                notifyItemRangeChanged(position, alumniList.size()); // Refresh the list
                                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Handle error
                                                Toast.makeText(context, "Error deleting from login table: " + response, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Handle error
                                            Toast.makeText(context, "Error Deleting Login Record", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            requestQueue.add(deleteLoginRequest);
                        } else {
                            // Handle error
                            Toast.makeText(context, "Error deleting from alumni details table: " + response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(context, "Error Deleting Alumni Record", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(deleteAlumniRequest);
    }

    public static class AlumniViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, dobTextView, genderTextView, collegeNameTextView, fieldOfStudyTextView, deptTextView, mobileNumberTextView, emailTextView;
        Button deleteButton;

        public AlumniViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView29);
            dobTextView = itemView.findViewById(R.id.textView30);
            genderTextView = itemView.findViewById(R.id.textView31);
            collegeNameTextView = itemView.findViewById(R.id.textView33);
            fieldOfStudyTextView = itemView.findViewById(R.id.textView42);
            deptTextView = itemView.findViewById(R.id.textView43);
            mobileNumberTextView = itemView.findViewById(R.id.textView44);
            emailTextView = itemView.findViewById(R.id.textView45);
            deleteButton = itemView.findViewById(R.id.button2); // Ensure this ID matches your layout
        }
    }
}
