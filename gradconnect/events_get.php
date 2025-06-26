<?php
// Include the database connection file
require 'dbconn.php';

// SQL query to get all columns except s_no
$sql = "SELECT `event_title`, `event_content`, `date` FROM `events`";

// Execute the query
$result = $conn->query($sql);

// Initialize an array to hold the results
$response = array();

if ($result->num_rows > 0) {
    // Fetch data and store it in the response array
    while ($row = $result->fetch_assoc()) {
        $response[] = $row;
    }
}

// Encode the response as JSON
header('Content-Type: application/json');
echo json_encode($response);

// Close the connection
$conn->close();
?>
