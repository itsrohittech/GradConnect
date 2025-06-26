<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

header('Content-Type: application/json');

// Retrieve the raw JSON input
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Validate and retrieve the values from the JSON data
$event_title = isset($data['event_title']) ? filter_var($data['event_title'], FILTER_SANITIZE_STRING) : '';
$event_content = isset($data['event_content']) ? filter_var($data['event_content'], FILTER_SANITIZE_STRING) : '';
$date = isset($data['date']) ? filter_var($data['date'], FILTER_SANITIZE_STRING) : ''; // Assuming the date is in a valid format

// Check if required fields are provided
if (!empty($event_title) && !empty($event_content) && !empty($date)) {
    // Prepare the SQL query to insert data
    $sql = "INSERT INTO events (event_title, event_content, date) VALUES (?, ?, ?)";

    // Prepare and bind the SQL statement
    if ($stmt = $conn->prepare($sql)) {
        $stmt->bind_param("sss", $event_title, $event_content, $date);

        // Execute the statement
        if ($stmt->execute()) {
            $response = array(
                'status' => 'success',
                'message' => 'Event added successfully'
            );
        } else {
            $response = array(
                'status' => 'error',
                'message' => 'Failed to add event. Please try again.'
            );
        }

        // Close the statement
        $stmt->close();
    } else {
        $response = array(
            'status' => 'error',
            'message' => 'Failed to prepare SQL statement for insertion'
        );
    }
} else {
    $response = array(
        'status' => 'error',
        'message' => 'Required fields are missing or invalid'
    );
}

// Close the connection
$conn->close();

// Output the response in JSON format
echo json_encode($response);
?>
