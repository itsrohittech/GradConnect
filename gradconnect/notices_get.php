<?php
require 'dbconn.php'; // Make sure this file contains the connection to your database

header('Content-Type: application/json');

$response = array();

$sql = "SELECT `notice_title`, `notice_content`, `notice_date` FROM `notices` WHERE 1";
$result = mysqli_query($conn, $sql);

$response = array();

if ($result->num_rows > 0) {
    // Fetch data and store it in the response array
    while ($row = $result->fetch_assoc()) {
        $response[] = $row;
    }
}

echo json_encode($response);

mysqli_close($conn);
?>
