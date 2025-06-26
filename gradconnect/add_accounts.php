<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Retrieve POST data
    $name = isset($_POST['name']) ? $_POST['name'] : null;
    $mobile_number = isset($_POST['mobile_number']) ? $_POST['mobile_number'] : null;
    $email = isset($_POST['email']) ? $_POST['email'] : null;
    $password = isset($_POST['password']) ? $_POST['password'] : null;

    // Validate the input
    if (empty($name) || empty($mobile_number) || empty($email) || empty($password)) {
        $response = array('message' => 'All fields are required');
        echo json_encode($response);
        exit;
    }

    // Prepare an SQL statement to prevent SQL injection
    $stmt = $conn->prepare("INSERT INTO login (name, mobile_number, email, password) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $name, $mobile_number, $email, $password);

    // Execute the statement
    if ($stmt->execute()) {
        $response = array('message' => 'Account added successfully');
    } else {
        $response = array('message' => 'Error: ' . $stmt->error);
    }

    // Close the statement
    $stmt->close();
} else {
    $response = array('message' => 'Invalid request method');
}

// Convert the response to JSON format
header('Content-Type: application/json');
echo json_encode($response);

// Close the database connection
$conn->close();
?>
