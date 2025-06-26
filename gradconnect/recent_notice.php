<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path to your actual database connection file

// Set the content type to JSON
header('Content-Type: application/json');

// Prepare the SQL query to fetch the most recent notice
$sql = "SELECT `s_no`, `notice_title`, `notice_content`, `notice_date` 
        FROM `notices` 
        ORDER BY `notice_date` DESC 
        LIMIT 1";

// Execute the query
$result = $conn->query($sql);

// Initialize response array
$response = array();

if ($result && $result->num_rows > 0) {
    // Fetch the most recent notice as an associative array
    $row = $result->fetch_assoc();
    
    // Populate the response array with fetched data
    $response['data'] = array(
        's_no' => $row['s_no'],
        'notice_title' => $row['notice_title'],
        'notice_content' => $row['notice_content'],
        'notice_date' => $row['notice_date']
    );
} else {
    $response['message'] = 'No records found';
}

// Close the connection
$conn->close();

// Output the JSON response
echo json_encode($response);
?>
