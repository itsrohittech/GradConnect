<?php
// Include database connection
require 'dbconn.php';

// Check if the request method is GET
if ($_SERVER["REQUEST_METHOD"] == "GET") {
    // Check if a parameter is provided
    if (isset($_GET['student_id']) || isset($_GET['email']) || isset($_GET['mobile_number'])) {
        // Determine which parameter is provided
        if (isset($_GET['student_id'])) {
            $parameter = "student_id";
            $value = $_GET['student_id'];
        } elseif (isset($_GET['email'])) {
            $parameter = "email";
            $value = $_GET['email'];
        } elseif (isset($_GET['mobile_number'])) {
            $parameter = "mobile_number";
            $value = $_GET['mobile_number'];
        }

        // Prepare the SQL query to fetch data based on the parameter
        $sql = "SELECT status, first_name, last_name, dob, gender, mobile_number, email, student_id, college_name, field_of_study, graduation_year, dept, address, city, state, country 
                FROM alumni_details 
                WHERE $parameter = ?";

        // Prepare the statement
        $stmt = $conn->prepare($sql);

        if ($stmt === false) {
            die(json_encode(array("message" => "Error preparing statement: " . $conn->error)));
        }

        // Bind the value to the prepared statement
        $stmt->bind_param("s", $value);

        // Execute the statement
        $stmt->execute();

        // Get the result
        $result = $stmt->get_result();

        // Check if any rows were returned
        if ($result->num_rows > 0) {
            // Fetch the data and store it in an array
            $alumniData = array();
            while ($row = $result->fetch_assoc()) {
                $alumniData[] = $row;
            }

            // Return the data in JSON format
            echo json_encode($alumniData);
        } else {
            // No data found for the given parameter
            echo json_encode(array("message" => "No data found for $parameter: $value"));
        }

        // Close the statement
        $stmt->close();
    } else {
        // Parameter is missing
        echo json_encode(array("message" => "Please provide either student_id, email, or mobile_number"));
    }
} else {
    // Invalid request method
    echo json_encode(array("message" => "Invalid request method"));
}

// Close the connection
$conn->close();
?>
