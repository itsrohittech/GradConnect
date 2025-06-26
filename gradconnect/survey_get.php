<?php
// Include the database connection file
require 'dbconn.php';

// Set content type to JSON for the response
header('Content-Type: application/json');

// Retrieve the mobile_number and options from the GET request
$mobile_number = isset($_GET['mobile_number']) ? $_GET['mobile_number'] : '';
$options = isset($_GET['options']) ? $_GET['options'] : '';

// Check if mobile_number and options are provided
if (!empty($mobile_number) && !empty($options)) {
    // Prepare the SQL query
    $sql = "SELECT `mobile_number`, `options`, `question_1`, `answer_1`, `question_2`, `answer_2`, `question_3`, `answer_3`, `question_4`, `answer_4`, `question_5`, `answer_5` 
            FROM `survey` 
            WHERE `mobile_number` = ? AND `options` = ?";

    // Prepare and bind the SQL statement
    $stmt = $conn->prepare($sql);
    if ($stmt) {
        $stmt->bind_param("ss", $mobile_number, $options);
        $stmt->execute();
        $result = $stmt->get_result();

        // Check if a row is returned
        if ($result->num_rows > 0) {
            // Fetch the row as an associative array
            $data = $result->fetch_assoc();

            // Return the data as a JSON response
            echo json_encode($data);
        } else {
            // No matching records found
            echo json_encode(array("message" => "No data found for the provided mobile number and option."));
        }

        // Close the statement
        $stmt->close();
    } else {
        // Error preparing the statement
        echo json_encode(array("message" => "Failed to prepare the SQL statement."));
    }
} else {
    // Handle missing parameters
    echo json_encode(array("message" => "Missing mobile_number or options parameter."));
}

// Close the database connection
$conn->close();
?>
