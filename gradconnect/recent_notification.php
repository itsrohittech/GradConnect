<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

header('Content-Type: application/json');

// Prepare the SQL query to fetch the most recent notification
$sql = "SELECT title, content, date FROM notifications ORDER BY date DESC LIMIT 1";

// Execute the query
$result = $conn->query($sql);

$response = array();

if ($result->num_rows > 0) {
    // Fetch the row and add to response array
    $row = $result->fetch_assoc();
    $response['data'] = $row;
} else {
    $response['message'] = 'No records found';
}

// Close the connection
$conn->close();

// Output the JSON response
echo json_encode($response);
?>
