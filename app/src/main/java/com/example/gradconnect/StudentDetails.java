package com.example.gradconnect;

public class StudentDetails {
    private String name;
    private String age;
    private String gender;
    private String collegeName;
    private String fieldOfStudy;
    private String mobileNumber;
    private String email;
    private String question1;
    private String answer1;
    private String question2;
    private String answer2;
    private String question3;
    private String answer3;
    private String question4;
    private String answer4;
    private String question5;
    private String answer5;

    // Constructor
    public StudentDetails(String name, String age, String gender, String collegeName, String fieldOfStudy, String mobileNumber, String email,
                          String question1, String answer1, String question2, String answer2, String question3, String answer3,
                          String question4, String answer4, String question5, String answer5) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.collegeName = collegeName;
        this.fieldOfStudy = fieldOfStudy;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.question1 = question1;
        this.answer1 = answer1;
        this.question2 = question2;
        this.answer2 = answer2;
        this.question3 = question3;
        this.answer3 = answer3;
        this.question4 = question4;
        this.answer4 = answer4;
        this.question5 = question5;
        this.answer5 = answer5;
    }

    // Getters
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getGender() { return gender; }
    public String getCollegeName() { return collegeName; }
    public String getFieldOfStudy() { return fieldOfStudy; }
    public String getMobileNumber() { return mobileNumber; }
    public String getEmail() { return email; }
    public String getQuestion1() { return question1; }
    public String getAnswer1() { return answer1; }
    public String getQuestion2() { return question2; }
    public String getAnswer2() { return answer2; }
    public String getQuestion3() { return question3; }
    public String getAnswer3() { return answer3; }
    public String getQuestion4() { return question4; }
    public String getAnswer4() { return answer4; }
    public String getQuestion5() { return question5; }
    public String getAnswer5() { return answer5; }
}
