<?php
session_start();

require_once('dynamodb_setup.php');

$eav = $marshaler->marshalJson('{":val": "' . $queryUserId . '"}');
$params = [
    'TableName' => $tableName,
    'ScanIndexForward' => $scanTableForwards,
    'KeyConditionExpression' => '#uid = :val',
    'ExpressionAttributeNames'=> [ '#uid' => 'userID' ],
    'ExpressionAttributeValues'=> $eav
];

$result = null;

try {
    $result = $dynamodb->query($params);
} catch (DynamoDbException | Exception $e) {
    $e = "Unable to query " . $tableName . " Table:<br>" . $e;
    require 'error_handler.php';
}

?>
