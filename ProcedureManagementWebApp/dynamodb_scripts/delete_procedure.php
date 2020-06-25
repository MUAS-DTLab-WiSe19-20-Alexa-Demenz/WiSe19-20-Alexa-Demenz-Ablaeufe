<?php
session_start();

if ($_SESSION["userId"] == null || $_GET["name"] == null) {
  header('Location: ../index.php');
  exit;
}

require 'dynamodb_setup.php';

$tableName = 'Procedures_Data';

$key = $marshaler->marshalJson('{"userID": "' . $_SESSION["userId"] . '","procedureName": "' . $_GET["name"] . '"}');
$params = ['TableName' => $tableName,'Key' => $key];

try {
    $result = $dynamodb->deleteItem($params);
} catch (DynamoDbException | Exception $e) {
    require 'error_handler.php';
}
header('Location: ../dashboard.php');
exit;
?>
