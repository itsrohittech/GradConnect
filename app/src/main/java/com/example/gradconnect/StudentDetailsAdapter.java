package com.example.gradconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.StudentDetailsViewHolder> {

    private ArrayList<StudentDetails> studentDetailsList;

    public StudentDetailsAdapter(ArrayList<StudentDetails> studentDetailsList) {
        this.studentDetailsList = studentDetailsList;
    }

    @NonNull
    @Override
    public StudentDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item view layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recycler, parent, false);
        return new StudentDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsViewHolder holder, int position) {
        // Get the current student details
        StudentDetails currentStudent = studentDetailsList.get(position);

        // Set student personal details
        holder.nameTextView.setText(currentStudent.getName());
        holder.ageTextView.setText("Date of Birth: " + currentStudent.getAge());
        holder.genderTextView.setText("Gender: " + currentStudent.getGender());
        holder.collegeNameTextView.setText("College: " + currentStudent.getCollegeName());
        holder.fieldOfStudyTextView.setText("Field of Study: " + currentStudent.getFieldOfStudy());
        holder.mobileTextView.setText("Mobile: " + currentStudent.getMobileNumber());
        holder.emailTextView.setText("Email: " + currentStudent.getEmail());

        // Set survey details
        holder.question1TextView.setText(currentStudent.getQuestion1());
        holder.answer1TextView.setText(currentStudent.getAnswer1());
        holder.question2TextView.setText(currentStudent.getQuestion2());
        holder.answer2TextView.setText(currentStudent.getAnswer2());
        holder.question3TextView.setText(currentStudent.getQuestion3());
        holder.answer3TextView.setText(currentStudent.getAnswer3());
        holder.question4TextView.setText(currentStudent.getQuestion4());
        holder.answer4TextView.setText(currentStudent.getAnswer4());
        holder.question5TextView.setText(currentStudent.getQuestion5());
        holder.answer5TextView.setText(currentStudent.getAnswer5());
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return studentDetailsList.size();
    }

    public static class StudentDetailsViewHolder extends RecyclerView.ViewHolder {

        // Personal details
        public TextView nameTextView;
        public TextView ageTextView;
        public TextView genderTextView;
        public TextView collegeNameTextView;
        public TextView fieldOfStudyTextView;
        public TextView mobileTextView;
        public TextView emailTextView;

        // Survey details
        public TextView question1TextView;
        public TextView answer1TextView;
        public TextView question2TextView;
        public TextView answer2TextView;
        public TextView question3TextView;
        public TextView answer3TextView;
        public TextView question4TextView;
        public TextView answer4TextView;
        public TextView question5TextView;
        public TextView answer5TextView;

        public StudentDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize personal details views
            nameTextView = itemView.findViewById(R.id.textView29);
            ageTextView = itemView.findViewById(R.id.textView30);
            genderTextView = itemView.findViewById(R.id.textView31);
            collegeNameTextView = itemView.findViewById(R.id.textView33);
            fieldOfStudyTextView = itemView.findViewById(R.id.textView42);
            mobileTextView = itemView.findViewById(R.id.textView43);
            emailTextView = itemView.findViewById(R.id.textView44);

            // Initialize survey details views
            question1TextView = itemView.findViewById(R.id.textView37);
            answer1TextView = itemView.findViewById(R.id.textView45);
            question2TextView = itemView.findViewById(R.id.textView46);
            answer2TextView = itemView.findViewById(R.id.textView47);
            question3TextView = itemView.findViewById(R.id.textView48);
            answer3TextView = itemView.findViewById(R.id.textView49);
            question4TextView = itemView.findViewById(R.id.textView50);
            answer4TextView = itemView.findViewById(R.id.textView51);
            question5TextView = itemView.findViewById(R.id.textView52);
            answer5TextView = itemView.findViewById(R.id.textView53);
        }
    }
}
