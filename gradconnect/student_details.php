<?php
// Include the database connection file
require 'dbconn.php'; // Adjust the path if necessary

// Check if the mobile_number parameter is provided
if (isset($_GET['mobile_number']) && !empty($_GET['mobile_number'])) {
    $mobile_number = $conn->real_escape_string($_GET['mobile_number']); // Escape user input

    // Define an array to hold the final results
    $results = array();

    // SQL query to fetch data from the `alumni_details` table
    $sql_alumni = "SELECT 
                        first_name, 
                        last_name, 
                        dob AS age, 
                        gender, 
                        college_name, 
                        field_of_study, 
                        mobile_number, 
                        email 
                   FROM 
                        alumni_details 
                   WHERE 
                        mobile_number = '$mobile_number'";
    $result_alumni = $conn->query($sql_alumni);

    // Check if alumni data was found
    if ($result_alumni->num_rows > 0) {
        $row_alumni = $result_alumni->fetch_assoc();

        // SQL query to fetch survey data related to this alumni
        $sql_survey = "SELECT 
                            question_1, 
                            answer_1, 
                            question_2, 
                            answer_2, 
                            question_3, 
                            answer_3, 
                            question_4, 
                            answer_4, 
                            question_5, 
                            answer_5 
                       FROM 
                            survey 
                       WHERE 
                            mobile_number = '$mobile_number'";
        $result_survey = $conn->query($sql_survey);

        // Check if survey data was found
        if ($result_survey->num_rows > 0) {
            $row_survey = $result_survey->fetch_assoc();
            // Combine alumni and survey data
            $combined_data = array_merge($row_alumni, $row_survey);
            $results = $combined_data;
        } else {
            // If no survey data is found, just include the alumni data
            $results = $row_alumni;
        }
    } else {
        // If no alumni data is found
        $results['message'] = 'No records found for the given mobile number';
    }
} else {
    $results['message'] = 'Mobile number is required';
}

// Convert the array to JSON format
header('Content-Type: application/json');
echo json_encode(array('data' => $results));

// Close the database connection
$conn->close();
?>
