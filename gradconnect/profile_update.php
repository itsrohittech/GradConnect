<?php
// Include the database connection file
require 'dbconn.php';

// Set the content type to JSON
header('Content-Type: application/json');

// Fetch the data from POST request
$mobile_number = $_POST['mobile_number']; // Ensure to validate/sanitize the input
$name = $_POST['name']; 
$college_name = $_POST['college_name'];
$email = $_POST['email'];
$password = $_POST['password'];

// Prepare response array
$response = array();

// Check if all required fields are provided
if (empty($mobile_number) || empty($name) || empty($college_name) || empty($email) || empty($password)) {
    $response['status'] = 'error';
    $response['message'] = 'All fields are required.';
    echo json_encode($response);
    exit();
}

// SQL query to update the details where the mobile_number matches
$sql = "UPDATE login SET name = ?, college_name = ?, email = ?, password = ? WHERE mobile_number = ?";

// Prepare the statement
$stmt = $conn->prepare($sql);
$stmt->bind_param("sssss", $name, $college_name, $email, $password, $mobile_number); // "sssss" indicates 5 strings

// Execute the statement
if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        // If the update was successful and rows were affected
        $response['status'] = 'success';
        $response['message'] = 'Details updated successfully.';
    } else {
        // If no rows were affected (mobile_number not found or no changes made)
        $response['status'] = 'error';
        $response['message'] = 'No changes made or mobile number not found.';
    }
} else {
    // If the update query failed
    $response['status'] = 'error';
    $response['message'] = 'Failed to update details.';
}

// Output the response as JSON
echo json_encode($response);

// Close the statement and connection
$stmt->close();
$conn->close();
?>
