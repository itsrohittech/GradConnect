<?php
// Include database connection
require 'dbconn.php'; 

// Query to fetch specific fields from alumni_details
$query = "SELECT CONCAT(first_name) AS name, dob, gender, college_name, field_of_study, dept, mobile_number, email 
          FROM alumni_details 
          WHERE 1";

// Execute the query
$result = mysqli_query($conn, $query);

// Check if query execution was successful
if (!$result) {
    echo json_encode(["error" => "Error fetching data: " . mysqli_error($conn)]);
    exit();
}

// Fetch the result and prepare data in JSON format
$studentDetails = array();
while ($row = mysqli_fetch_assoc($result)) {
    $studentDetails[] = $row;
}

// Output the data in JSON format
echo json_encode($studentDetails);

// Close the database connection
mysqli_close($conn);
?>
