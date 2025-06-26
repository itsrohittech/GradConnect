<?php
// Include the database connection file
require 'dbconn.php';

// Set the content type to JSON
header('Content-Type: application/json');

// Fetch mobile_number from the request (e.g., from a GET request)
$mobile_number = $_GET['mobile_number']; // Ensure to validate/sanitize this input

// SQL query to fetch the specific details
$sql = "SELECT name, college_name, email, mobile_number, password FROM login WHERE mobile_number = ?";

// Prepare and execute the SQL statement
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $mobile_number); // "s" indicates string data type
$stmt->execute();
$result = $stmt->get_result();

// Initialize response array
$response = array();

if ($result->num_rows > 0) {
    // Fetch the row as an associative array
    $row = $result->fetch_assoc();
    
    // Populate the response array with fetched data
    $response['data'] = array(
        'name' => $row['name'],
        'college_name' => $row['college_name'],
        'email' => $row['email'],
        'mobile_number' => $row['mobile_number'],
        'password' => $row['password']
    );
}

// Output the response in JSON format
echo json_encode($response);

// Close the statement and the connection
$stmt->close();
$conn->close();
?>
