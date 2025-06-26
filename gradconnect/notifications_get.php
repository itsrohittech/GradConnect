<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

header('Content-Type: application/json');

// Prepare the SQL query to fetch data
$sql = "SELECT title, content, date FROM notifications WHERE 1";

// Execute the query
$result = $conn->query($sql);

$response = array();

if ($result->num_rows > 0) {
    // Fetch data and add to response array
    while ($row = $result->fetch_assoc()) {
        $response[] = $row;
    }
} else {
    $response['message'] = 'No records found';
}

// Close the connection
$conn->close();

// Output the JSON response
echo json_encode($response);
?>
