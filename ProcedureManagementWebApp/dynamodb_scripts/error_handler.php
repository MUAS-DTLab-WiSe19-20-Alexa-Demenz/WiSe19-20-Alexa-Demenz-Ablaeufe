<?php
session_start();
$_SESSION["errorMessage"] = $e->getMessage();
header('Location: ../error.php');
exit;
?>
