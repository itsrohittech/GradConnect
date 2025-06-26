package com.example.gradconnect;

public class AlumniVeri {
    private String name;
    private String dob;
    private String gender;
    private String collegeName;
    private String fieldOfStudy;
    private String dept;
    private String mobileNumber;
    private String email;

    public AlumniVeri(String name, String dob, String gender, String collegeName, String fieldOfStudy, String dept, String mobileNumber, String email) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.collegeName = collegeName;
        this.fieldOfStudy = fieldOfStudy;
        this.dept = dept;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public String getDept() {
        return dept;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }
}
