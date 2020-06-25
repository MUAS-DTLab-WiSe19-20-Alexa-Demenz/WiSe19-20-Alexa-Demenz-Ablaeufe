<?php
session_start();

if ($_SESSION["userId"] == null || $_POST["submit_name"] == null) {
  header('Location: ../index.php');
  exit;
}

require 'dynamodb_setup.php';

$tableName = 'Procedures_Data';
$key = $marshaler->marshalJson('{"userID": "' . $_SESSION["userId"] . '","procedureName": "' . $_POST["submit_name"] . '"}');
$params = ['TableName' => $tableName,'Key' => $key];

$result = null;

try {
    $result = $dynamodb->getItem($params);
} catch (DynamoDbException | Exception $e) {
    require 'error_handler.php';
}

$_POST["submit_json"] = addcslashes($_POST["submit_json"],'"');

if ($result["Item"] == null) { // create new procedure
    $item = $marshaler->marshalJson('
      {
          "userID": "' . $_SESSION["userId"] . '",
          "procedureName": "' . $_POST["submit_name"] . '",
          "jsonFile": "' .  $_POST["submit_json"] . '"
      }
    ');
    $params = ['TableName' => $tableName,'Item' => $item];
    try {
      $result = $dynamodb->putItem($params);
    } catch (DynamoDbException | Exception $e) {
      require 'error_handler.php';
    }

} else { // update existing procedure
    $params = [
        'TableName' => $tableName,
        'Key' => $key,
        'UpdateExpression' => 'set jsonFile = :jsonVal',
        'ExpressionAttributeValues'=> $marshaler->marshalJson('{":jsonVal": "' . $_POST["submit_json"] . '"}'),
        'ReturnValues' => 'NONE'
    ];
    try {
        $result = $dynamodb->updateItem($params);
    } catch (DynamoDbException | Exception $e) {
      require '/error_handler.php';
    }
}

header('Location: ../dashboard.php');
exit;

?>
