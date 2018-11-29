<?php
    $con=mysqli_connect("menuappdb.cctlbaybdt7x.us-east-2.rds.amazonaws.com", "menuadmin", "menuappadmin4", "menuappdb");

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }

    $sql = $_GET['query'];

    if (mysqli_query($con, $sql)) {
        echo "Values have been inserted successfully!";
    }

    mysqli_close($con);
?>
