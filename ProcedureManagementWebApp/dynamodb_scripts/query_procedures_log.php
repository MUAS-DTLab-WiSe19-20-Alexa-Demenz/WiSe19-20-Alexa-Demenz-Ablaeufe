<?php
session_start();

$tableName = 'Procedures_Log';
$queryUserId = $_SESSION["userId"];
$scanTableForwards = false;
require 'query_dynamodb.php';

$VISIBLE_ROWS = 15;
$rowCounter = 0;
$styleTag = '';

foreach ($result['Items'] as $entry) {
    if ($rowCounter === $VISIBLE_ROWS) {
      $styleTag = ' style="display:none;"';
      echo '<tr><td colspan=5><a onclick="extendList(this)">Mehr anzeigen</a></td></tr>' . "\n";
    }
    $startTimestamp = $marshaler->unmarshalValue($entry['startTime']);
    $endTimestamp = $marshaler->unmarshalValue($entry['endTime']);
    echo "<tr$styleTag><td>" . $marshaler->unmarshalValue($entry['procedureName']) . "</td>" .
    "<td>" . date("d.m.Y", $startTimestamp) . "</td><td>" . date("H:i", $startTimestamp) . "</td><td>" . date("H:i", $endTimestamp) . "</td>" .
    "<td>" . $marshaler->unmarshalValue($entry['endState']) . "</td></tr>\n";
    ++$rowCounter;
}
?>
