<?php
session_start();
if ($_SESSION["userId"] == null) {
  header('Location: index.php');
  exit;
}

$GET_procObj = null;
$GET_name = "";
$GET_trigger_days = "";
$GET_trigger_time = "";
$GET_steps = [];
$lastGETStepIndex = null;

if ($_GET["edit_name"] != null) {

    require './dynamodb_scripts/dynamodb_setup.php';

    $getItemUserId = $_GET["edit_origin"] == "user" ? $_SESSION["userId"] : "Template";
    $tableName = 'Procedures_Data';
    $key = $marshaler->marshalJson('{"userID": "' . $getItemUserId . '","procedureName": "' . $_GET["edit_name"] . '"}');
    $params = ['TableName' => $tableName,'Key' => $key];

    $result = null;
    try {
        $result = $dynamodb->getItem($params);
    } catch (DynamoDbException | Exception $e) {
        require 'error_handler.php';
    }

    if ($result["Item"] != null) {
        $proc = $result["Item"];
        $GET_procObj = json_decode($marshaler->unmarshalValue($proc['jsonFile']));
        $GET_name = $GET_procObj->procedureName;
        $GET_trigger_days = $GET_procObj->triggerParameters[0]->DAYS;
        $GET_trigger_time = $GET_procObj->triggerParameters[0]->TIME;
        $GET_steps = $GET_procObj->steps;
        $lastGETStepIndex = count($GET_steps) - 1;
    }

    $_GET["edit_name"] = null;
    $_GET["edit_origin"] = null;
}
?>
<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex,nofollow">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ablauf Hilfe | Ablauf-Editor</title>
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
  </head>
  <header>
    <?php require 'navigation.php'; ?>
  </header>
  <body>
    <?php require 'amazon_login_script.html'; ?>
    <div class="container">
    <h1>Ablauf-Editor</h1>
    <div id="editor">
      <p style="margin:-10px 0 -10px 0;"><img src="warning-sign.png" style="width:28px;margin-bottom:-6px;">&ensp;Wenn bereits ein Ablauf mit dem angegebenen Namen existiert wird dieser <strong style="padding:0;">ohne Warnung unwiderruflich ersetzt!</strong></p>
      <p style="margin:-10px 0 -10px 0;">Die Ablauftexte können mittels Amazons SpeechSynthesisMarkupLanguage (SSML) erweitert werden. Mehr dazu auf <a href="https://developer.amazon.com/de-DE/docs/alexa/custom-skills/speech-synthesis-markup-language-ssml-reference.html">Amazon</a>></p>
      <form id="procedure_form">
        <h4>Name</h4>
        <table id="name_table">
          <thead>
            <tr>
              <th class="desc"></th>
              <th class="txt"></th>
              <th class="btn"></th>
            </tr>
          </thead>
          <tr>
            <td><strong>Aktivierungsname</strong></td> <td><input type="text" id="name" value="<?php echo $GET_name;?>"><br></td>
            <td><button type="button" class="help_btn" onclick="alert(name_help)">?</button></td>
          </tr>
        </table>
        <h4>Auslöser</h4>
        <p>&emsp;Die Verwenung von Auslösern wird momentan noch nicht unterstützt.</p>
        <table id="trigger_table">
          <thead>
            <tr>
              <th class="desc"></th>
              <th class="txt"></th>
              <th class="btn"></th>
            </tr>
          </thead>
          <tr>
            <td><strong>Wiederholen</strong></td>
            <td>
              <select name="repeat_list" form="procedure_form">
                <option value="NEVER" <?php if ($GET_trigger_days == "NEVER") echo "selected"; ?>>Nie</option>
                <option value="EVERYDAY" <?php if ($GET_trigger_days == "EVERYDAY") echo "selected"; ?>>Täglich</option>
                <option disabled></option>
                <option value="MONDAYS"<?php if ($GET_trigger_days == "MONDAYS") echo " selected"; ?>>Montags</option>
                <option value="TUESDAYS"<?php if ($GET_trigger_days == "TUESDAYS") echo " selected"; ?>>Dienstags</option>
                <option value="WEDNESDAYS"<?php if ($GET_trigger_days == "WEDNESDAYS") echo " selected"; ?>>Mittwochs</option>
                <option value="THURSDAYS"<?php if ($GET_trigger_days == "THURSDAYS") echo " selected"; ?>>Donnerstags</option>
                <option value="FRIDAYS"<?php if ($GET_trigger_days == "FRIDAYS") echo " selected"; ?>>Freitags</option>
                <option value="SATURDAYS"<?php if ($GET_trigger_days == "SATURDAYS") echo " selected"; ?>>Samstags</option>
                <option value="SUNDAYS"<?php if ($GET_trigger_days == "SUNDAYS") echo " selected"; ?>>Sonntags</option>
              </select>
            </td>
            <td><button type="button" class="help_btn" onclick="alert(repeat_help)">?</button></td>
          </tr>
          <tr>
            <td><strong>Uhrzeit</strong></td> <td><input type="number" id="trigger_time" value="<?php echo $GET_trigger_time;?>"></td>
            <td><button type="button" class="help_btn" onclick="alert(time_help)">?</button></td>
          </tr>
        </table>
        <h4>Schritte</h4>
        <table id="steps_table">
          <thead>
            <tr>
              <th class="desc"></th>
              <th class="txt"></th>
              <th class="btn"></th>
            </tr>
          </thead>
          <?php
          $stepNum = 1;
          $instrTxt = "";
          $helpTxt = "";
          $confTxt = "";
          if (count($GET_steps) === 0) {
              require 'procedure_step.php';
          } else {
              for ($i = 0; $i < $lastGETStepIndex; $i++) {
                  $stepNum = $i + 1;
                  $instrTxt = $GET_steps[$i]->instructionText;
                  $helpTxt = $GET_steps[$i]->helpText;
                  $confTxt = $GET_steps[$i]->confirmText;
                  require 'procedure_step.php';
              }
          }
          ?>
          <tbody id="final_step">
            <tr>
              <td><h5>Letzter Schritt</h5></td>
            </tr>
            <tr>
              <td><strong>Abschlusstext</strong></td><td><textarea type="text" id="fin_txt"><?php if(count($GET_steps) > 0) echo $GET_steps[$lastGETStepIndex]->finishedText;?></textarea></td>
              <td><button type="button" class="help_btn" onclick="alert(fin_txt_help)">?</button></td>
            </tr>
          </tbody>
        </table>
      </form>
      <form method="post" action="./dynamodb_scripts/save_procedure.php">
        <button type="button" id="save_button" onclick="processProcedure()">Speichern</button>
        <input type="hidden" name="submit_name" value="">
        <input type="hidden" name="submit_json" value="">
      </form>
    </div>
    <div id="editor_templates">
        <h4>Vorlagen</h4>
        <ol>
            <?php
            $tableName = 'Procedures_Data';
            $queryUserId = "Template";
            $scanTableForwards = true;
            require './dynamodb_scripts/query_dynamodb.php';
            foreach ($result['Items'] as $procedure) {
                echo "<li><a onclick=\"editProcedure('template', this)\">" . $marshaler->unmarshalValue($procedure['procedureName']) . "</a></li>\n";
            }
            ?>
        </ol>
    </div>
    </div>
  </body>
  <footer>
    <?php require 'footer.php'; ?>
  </footer>
</html>
