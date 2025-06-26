<?php
// Include the database connection
require 'dbconn.php';

// Initialize response array
$response = array();

// Check if either mobile number or username is provided
if (isset($_POST['mobile_number']) && !empty($_POST['mobile_number'])) {
    // User input is a mobile number
    $input_field = "mobile_number";
    $user_input = $_POST['mobile_number'];
} elseif (isset($_POST['username']) && !empty($_POST['username'])) {
    // User input is a username
    $input_field = "username";
    $user_input = $_POST['username'];
} else {
    // If neither mobile number nor username is provided
    $response['status'] = "error";
    $response['message'] = "Please enter either a username or mobile number.";
    echo json_encode($response);
    exit;
}

// Check if password is provided
if (!isset($_POST['password']) || empty($_POST['password'])) {
    $response['status'] = "error";
    $response['message'] = "Password cannot be empty.";
    echo json_encode($response);
    exit;
}

$user_password = $_POST['password'];

// SQL query to check if the mobile number/username and password match
$sql = "SELECT * FROM login WHERE $input_field = ? AND password = ?";

// Prepare and bind the statement
$stmt = $conn->prepare($sql);
if ($stmt === false) {
    $response['status'] = "error";
    $response['message'] = "Database error: Unable to prepare the statement.";
    echo json_encode($response);
    exit;
}
$stmt->bind_param("ss", $user_input, $user_password);

// Execute the statement
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // Mobile number/username and password match
    $user_data = $result->fetch_assoc();
    $response['status'] = "success";
    $response['message'] = "Login successful!";
  // Optionally return user data
} else {
    // Invalid mobile number/username or password
    $response['status'] = "error";
    $response['message'] = "Invalid username, mobile number, or password.";
}

// Close the statement and connection
$stmt->close();
$conn->close();

// Output the response as JSON
echo json_encode($response);
?>
