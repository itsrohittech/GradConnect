<?php
// Include the database connection file
require 'dbconn.php';

// Set the content type to JSON
header('Content-Type: application/json');

// Retrieve POST data
$username = isset($_POST['username']) ? $_POST['username'] : '';
$name = isset($_POST['name']) ? $_POST['name'] : '';
$college_name = isset($_POST['college_name']) ? $_POST['college_name'] : '';
$email = isset($_POST['email']) ? $_POST['email'] : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';
$mobile_number = isset($_POST['mobile_number']) ? $_POST['mobile_number'] : ''; // Added mobile_number

// Validate and sanitize inputs
$username = filter_var($username, FILTER_SANITIZE_STRING);
$name = filter_var($name, FILTER_SANITIZE_STRING);
$college_name = filter_var($college_name, FILTER_SANITIZE_STRING);
$email = filter_var($email, FILTER_SANITIZE_EMAIL);
$password = filter_var($password, FILTER_SANITIZE_STRING);
$mobile_number = filter_var($mobile_number, FILTER_SANITIZE_STRING); // Added sanitization for mobile_number

// Check if username is provided
if (!empty($username)) {
    // Prepare the SQL query to update data
    $sql = "UPDATE login SET 
                name = ?, 
                college_name = ?, 
                email = ?, 
                password = ?, 
                mobile_number = ? 
            WHERE username = ?";

    // Prepare and bind the SQL statement
    if ($stmt = $conn->prepare($sql)) {
        $stmt->bind_param("ssssss", $name, $college_name, $email, $password, $mobile_number, $username);

        // Execute the statement
        $stmt->execute();
        
        // Check if any rows were affected
        if ($stmt->affected_rows > 0) {
            $response = array(
                'status' => 'success',
                'message' => 'User data updated successfully'
            );
        } else {
            $response = array(
                'status' => 'error',
                'message' => 'No rows updated. Username might be incorrect or data might be the same'
            );
        }

        // Close the statement
        $stmt->close();
    } else {
        $response = array(
            'status' => 'error',
            'message' => 'Failed to prepare SQL statement for update'
        );
    }
} else {
    $response = array(
        'status' => 'error',
        'message' => 'Username parameter is missing or invalid'
    );
}

// Close the connection
$conn->close();

// Output the response in JSON format
echo json_encode($response);
?>
