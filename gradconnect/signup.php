<?php
// Database connection
require 'dbconn.php';

// Check if request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Collect the posted data
    $name = isset($_POST['name']) ? $_POST['name'] : '';
    $email = isset($_POST['email']) ? $_POST['email'] : '';
    $mobile_number = isset($_POST['mobile_number']) ? $_POST['mobile_number'] : '';
    $username = isset($_POST['username']) ? $_POST['username'] : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';

    // Check if all fields are provided
    if (!empty($name) && !empty($email) && !empty($mobile_number) && !empty($username) && !empty($password)) {
        // Prepare the SQL query
        $sql = "INSERT INTO `login` (`name`, `email`, `mobile_number`, `username`, `password`) 
                VALUES (?, ?, ?, ?, ?)";

        // Prepare the statement
        $stmt = $conn->prepare($sql);
        
        if ($stmt) {
            // Bind parameters and execute the query
            $stmt->bind_param("sssss", $name, $email, $mobile_number, $username, $password);

            // Execute the query
            if ($stmt->execute()) {
                echo json_encode(["status" => "success", "message" => "User successfully registered."]);
            } else {
                echo json_encode(["status" => "error", "message" => "Failed to insert data: " . $stmt->error]);
            }

            // Close the statement
            $stmt->close();
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "All fields are required."]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method."]);
}

// Close the database connection
$conn->close();
?>
