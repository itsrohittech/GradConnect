<?php
$servername = "localhost";
$username = "root";
$password = "";
$db = "gradconnect";

// Create connection
$conn = new mysqli($servername, $username, $password, $db);

// Check connection
$response = array();
if ($conn->connect_error) {
    $response['status'] = "error";
    $response['message'] = "Connection failed: " . $conn->connect_error;
} else {
    $response['status'] = "success";
    $response['message'] = "Connected successfully";
}

// Convert the response array to JSON
$json_response = json_encode($response);

// Output the JSON response
//echo $json_response;
?>