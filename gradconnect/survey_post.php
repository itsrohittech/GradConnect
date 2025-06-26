<?php
// Include the database connection file
require 'dbconn.php';  // Adjust the path if necessary

// Initialize the response array
$response = array();

// Retrieve POST data
$mobile_number = isset($_POST['mobile_number']) ? $_POST['mobile_number'] : '';
$options = isset($_POST['options']) ? $_POST['options'] : '';
$question_1 = isset($_POST['question_1']) ? $_POST['question_1'] : '';
$answer_1 = isset($_POST['answer_1']) ? $_POST['answer_1'] : '';
$question_2 = isset($_POST['question_2']) ? $_POST['question_2'] : '';
$answer_2 = isset($_POST['answer_2']) ? $_POST['answer_2'] : '';
$question_3 = isset($_POST['question_3']) ? $_POST['question_3'] : '';
$answer_3 = isset($_POST['answer_3']) ? $_POST['answer_3'] : '';
$question_4 = isset($_POST['question_4']) ? $_POST['question_4'] : '';
$answer_4 = isset($_POST['answer_4']) ? $_POST['answer_4'] : '';
$question_5 = isset($_POST['question_5']) ? $_POST['question_5'] : '';
$answer_5 = isset($_POST['answer_5']) ? $_POST['answer_5'] : '';

// Validate and sanitize inputs
$mobile_number = filter_var($mobile_number, FILTER_SANITIZE_STRING);
$options = filter_var($options, FILTER_SANITIZE_STRING);
$question_1 = filter_var($question_1, FILTER_SANITIZE_STRING);
$answer_1 = filter_var($answer_1, FILTER_SANITIZE_STRING);
$question_2 = filter_var($question_2, FILTER_SANITIZE_STRING);
$answer_2 = filter_var($answer_2, FILTER_SANITIZE_STRING);
$question_3 = filter_var($question_3, FILTER_SANITIZE_STRING);
$answer_3 = filter_var($answer_3, FILTER_SANITIZE_STRING);
$question_4 = filter_var($question_4, FILTER_SANITIZE_STRING);
$answer_4 = filter_var($answer_4, FILTER_SANITIZE_STRING);
$question_5 = filter_var($question_5, FILTER_SANITIZE_STRING);
$answer_5 = filter_var($answer_5, FILTER_SANITIZE_STRING);

// Check if all necessary fields are provided
if (!empty($mobile_number) && !empty($options)) {
    // Check if the record exists
    $select_sql = "SELECT * FROM survey WHERE mobile_number = ? AND options = ?";
    if ($select_stmt = $conn->prepare($select_sql)) {
        $select_stmt->bind_param("ss", $mobile_number, $options);
        $select_stmt->execute();
        $result = $select_stmt->get_result();
        
        if ($result->num_rows > 0) {
            // Prepare the SQL query to update data
            $sql = "UPDATE survey SET 
                        question_1 = ?, answer_1 = ?, 
                        question_2 = ?, answer_2 = ?, 
                        question_3 = ?, answer_3 = ?, 
                        question_4 = ?, answer_4 = ?, 
                        question_5 = ?, answer_5 = ? 
                    WHERE mobile_number = ? AND options = ?";

            // Prepare and bind the SQL statement
            if ($stmt = $conn->prepare($sql)) {
                $stmt->bind_param(
                    "ssssssssssss", // 11 's' for 11 parameters
                    $question_1, $answer_1,
                    $question_2, $answer_2,
                    $question_3, $answer_3,
                    $question_4, $answer_4,
                    $question_5, $answer_5,
                    $mobile_number, $options
                );
                
                // Execute the statement
                $stmt->execute();
                
                // Check if any rows were affected
                if ($stmt->affected_rows > 0) {
                    $response['status'] = 'success';
                    $response['message'] = 'Data updated successfully';
                } else {
                    $response['status'] = 'error';
                    $response['message'] = 'No rows updated. Data may be the same';
                }

                // Close the statement
                $stmt->close();
            } else {
                $response['status'] = 'error';
                $response['message'] = 'Failed to prepare SQL statement for update';
            }
        } else {
            $response['status'] = 'error';
            $response['message'] = 'No record found with the provided mobile_number and options';
        }
        $select_stmt->close();
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Failed to prepare SQL statement for select query';
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Mobile number or options parameter is missing or invalid';
}

// Close the connection
$conn->close();

// Set the content type to application/json
header('Content-Type: application/json');

// Encode the response array to JSON and output it
echo json_encode($response);
?>
