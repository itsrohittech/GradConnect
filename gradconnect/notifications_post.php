<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

// Initialize response array
$response = array();

// Handle POST request to insert a new notification
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get the raw POST data
    $rawData = file_get_contents("php://input");
    $data = json_decode($rawData, true);

    // Retrieve data from the JSON
    $title = isset($data['title']) ? $data['title'] : '';
    $content = isset($data['content']) ? $data['content'] : '';
    $date = isset($data['date']) ? $data['date'] : '';

    if (!empty($title) && !empty($content) && !empty($date)) {
        try {
            // SQL query to insert a new notification
            $stmt = $conn->prepare("INSERT INTO `notifications` (`title`, `content`, `date`) VALUES (?, ?, ?)");
            $stmt->bind_param("sss", $title, $content, $date);

            if ($stmt->execute()) {
                $response['status'] = 'success';
                $response['message'] = 'Notification published successfully!';
            } else {
                $response['status'] = 'error';
                $response['message'] = 'Failed to publish notification: ' . $stmt->error;
            }

            $stmt->close();
        } catch (Exception $e) {
            // Catch any exceptions
            $response['status'] = 'error';
            $response['message'] = 'Exception: ' . $e->getMessage();
        }
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Title, content, and date cannot be empty';
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Handle GET request to fetch notifications
    try {
        // SQL query to select columns except `s_no`
        $sql = "SELECT `title`, `content`, `date` FROM `notifications`";

        // Execute the query
        $result = $conn->query($sql);

        if ($result) {
            // Check if there are results
            if ($result->num_rows > 0) {
                // Output data for each row
                $notifications = array();
                while ($row = $result->fetch_assoc()) {
                    $notifications[] = $row;
                }
                // Set response status and data
                $response['status'] = 'success';
                $response['data'] = $notifications;
            } else {
                // No results found
                $response['status'] = 'success';
                $response['data'] = array(); // Empty array for no results
                $response['message'] = 'No notifications found';
            }
        } else {
            // Query failed
            $response['status'] = 'error';
            $response['message'] = 'Query failed: ' . $conn->error;
        }
    } catch (Exception $e) {
        // Catch any exceptions
        $response['status'] = 'error';
        $response['message'] = 'Exception: ' . $e->getMessage();
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Invalid request method';
}

// Convert the result to JSON format
header('Content-Type: application/json');
echo json_encode($response);

// Close the connection
$conn->close();
?>
