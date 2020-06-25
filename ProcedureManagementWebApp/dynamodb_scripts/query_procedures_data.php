<?php
session_start();

$tableName = 'Procedures_Data';
$queryUserId = $_SESSION["userId"];
$scanTableForwards = true;
require 'query_dynamodb.php';

foreach ($result['Items'] as $procedure) {
    echo "<tr>\n<td>" . $marshaler->unmarshalValue($procedure['procedureName']) . "</td>\n" .
    "<td><button type=\"button\" class=\"edit_btn\" onclick=\"editProcedure('user', this)\">Bearbeiten</button></td>\n" .
    "<td><button type=\"button\" class=\"delete_btn\" onclick=\"deleteProcedure(this)\">Löschen</button></td>\n" .
    "<td><button type=\"button\" class=\"show_btn\" onclick=\"expandRow(this)\">&#9666;</button></td>\n</tr>\n";

    $proc = json_decode($marshaler->unmarshalValue($procedure['jsonFile']));
    $steps = $proc->steps;
    $lastStepIndex = count($steps) - 1;
    $triggerTime = "" . $proc->triggerParameters[0]->TIME;
    if ($triggerTime != "") $triggerTime = ", " . substr($triggerTime, 0, 2) . ":" . substr($triggerTime, -2) . " Uhr";
    $detailsTable = "<table id=\"procedure_detail_table\">\n" .
    "<tr id=\"heading_row\"><td colspan=\"2\">Auslöser</td></tr>\n" .
    "<tr><td colspan=\"2\">" . $proc->triggerParameters[0]->DAYS . $triggerTime . "</td></tr>\n";
    for ($i = 0; $i < $lastStepIndex; $i++) {
      $detailsTable .= "<tr id=\"heading_row\"><td colspan=\"2\">Schritt " . ($i+1) . "</td></tr>\n" .
      "<tr><td>Anweisungstext</td><td>" . $steps[$i]->instructionText . "</td></tr>\n" .
      "<tr><td>Hilfetext</td><td>" . $steps[$i]->helpText . "</td></tr>\n" .
      "<tr><td>Bestätigungsfrage</td><td>" . $steps[$i]->confirmText . "</td></tr>";
    }
    $detailsTable .= "<tr id=\"heading_row\"><td colspan=\"2\">Letzter Schritt</td></tr>\n" .
    "<tr><td>Abschlusstext</td><td>" . $steps[$lastStepIndex]->finishedText . "</td></tr>\n" .
    "</table>";
    echo "<tr style=\"display:none;\"><td colspan=\"4\">" . $detailsTable . "</td></tr>\n";
}
?>
