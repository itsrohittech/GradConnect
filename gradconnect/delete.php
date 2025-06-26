<?php
require 'dbconn.php'; // Database connection

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $firstName = $_GET['first_name']; // Get the first name from the URL query string

    if (!empty($firstName)) {
        // Begin a transaction to ensure the deletion is handled properly
        $conn->begin_transaction();
        try {
            // Delete from the alumni_details table
            $sql = "DELETE FROM alumni_details WHERE first_name = ?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param('s', $firstName);
            if (!$stmt->execute()) {
                throw new Exception("Error deleting from alumni_details table: " . $conn->error);
            }

            // Commit the transaction
            $conn->commit();
            echo "Alumni details deleted successfully";
        } catch (Exception $e) {
            // Rollback the transaction if something went wrong
            $conn->rollback();
            echo "Error: " . $e->getMessage();
        }
    } else {
        echo "No first name provided";
    }
} else {
    echo "Invalid request method";
}
?>
