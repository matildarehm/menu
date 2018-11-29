<?php
    $con=mysqli_connect("menuappdb.cctlbaybdt7x.us-east-2.rds.amazonaws.com", "menuadmin", "menuappadmin4", "menuappdb");

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }

    $sql = $_GET['query'];

    if ($result = mysqli_query($con, $sql)) {
        $resultArray = array();
        $tempArray = array();

        while ($row = $result->fetch_object()) {
            $tempArray = $row;
            array_push($resultArray, $tempArray);
        }

        echo json_encode($resultArray);
    }

    mysqli_close($con);
?>
