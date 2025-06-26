<?php
// Include database connection
require 'dbconn.php';

// Prepare and bind
$stmt = $conn->prepare("INSERT INTO alumni_details (first_name, last_name, dob, gender, mobile_number, email, student_id, college_name, field_of_study, graduation_year, dept, address, city, state, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

// Check if prepare was successful
if ($stmt === false) {
    die("Prepare failed: " . $conn->error);
}

// Bind parameters
$stmt->bind_param("sssssssssssssss", $first_name, $last_name, $dob, $gender, $mobile_number, $email, $student_id, $college_name, $field_of_study, $graduation_year, $dept, $address, $city, $state, $country);

// Set parameters and execute
$first_name = $_POST['first_name'];
$last_name = $_POST['last_name'];
$dob = $_POST['dob'];
$gender = $_POST['gender'];
$mobile_number = $_POST['mobile_number'];
$email = $_POST['email'];
$student_id = $_POST['student_id'];
$college_name = $_POST['college_name'];
$field_of_study = $_POST['field_of_study'];
$graduation_year = $_POST['graduation_year'];
$dept = $_POST['dept'];
$address = $_POST['address'];
$city = $_POST['city'];
$state = $_POST['state'];
$country = $_POST['country'];

// Execute the statement
if ($stmt->execute()) {
    echo "New record created successfully";
} else {
    echo "Error: " . $stmt->error;
}

// Close statement and connection
$stmt->close();
$conn->close();
?>
