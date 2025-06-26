<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

header('Content-Type: application/json');

// Get the raw POST data
$input = file_get_contents('php://input');
$data = json_decode($input, true); // Decode JSON data

// Retrieve data from the decoded JSON
$notice_title = isset($data['notice_title']) ? $data['notice_title'] : '';
$notice_content = isset($data['notice_content']) ? $data['notice_content'] : '';
$notice_date = isset($data['notice_date']) ? $data['notice_date'] : '';

// Validate and sanitize inputs
$notice_title = filter_var($notice_title, FILTER_SANITIZE_STRING);
$notice_content = filter_var($notice_content, FILTER_SANITIZE_STRING);
$notice_date = filter_var($notice_date, FILTER_SANITIZE_STRING); // Assuming date is in valid format (YYYY-MM-DD)

// Debugging: Log received data
error_log("Received data: Title=$notice_title, Content=$notice_content, Date=$notice_date");

// Check if required fields are provided
if (!empty($notice_title) && !empty($notice_content) && !empty($notice_date)) {
    // Prepare the SQL query to insert data
    $sql = "INSERT INTO notices (notice_title, notice_content, notice_date) VALUES (?, ?, ?)";

    // Prepare and bind the SQL statement
    if ($stmt = $conn->prepare($sql)) {
        $stmt->bind_param("sss", $notice_title, $notice_content, $notice_date);

        // Execute the statement
        if ($stmt->execute()) {
            $response = array(
                'status' => 'success',
                'message' => 'Notice added successfully'
            );
        } else {
            $response = array(
                'status' => 'error',
                'message' => 'Failed to add notice. Please try again.'
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
