<?php
session_start();
if ($_SESSION["userId"] == null) {
  header('Location: index.php');
  exit;
}
?>
<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex,nofollow">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ablauf Hilfe | Dashboard</title>
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
  </head>
  <header>
    <?php require 'navigation.php'; ?>
  </header>
  <body>
    <?php require 'amazon_login_script.html'; ?>
    <div class="container">
      <h2><?php echo "Hallo " . $_SESSION["userName"];?></h2>
      <table id="procedure_list_table">
        <caption>Eigene Abläufe</caption>
        <thead>
          <tr>
            <th class="name"></th>
            <th class="edit"></th>
            <th class="delete"></th>
            <th class="show"></th>
          </tr>
        </thead>
        <tbody>
<?php require './dynamodb_scripts/query_procedures_data.php'; ?>
        </tbody>
      </table>
      <table id="log_table">
        <caption>Nutzerprotokoll</caption>
        <thead>
          <tr>
            <th>Ablauf</th><th>Datum</th><th>Startzeit</th><th>Endzeit</th><th>Status</th>
          </tr>
        </thead>
        <tbody>
<?php require './dynamodb_scripts/query_procedures_log.php'; ?>
        </tbody>
      </table>
    </div>
  </body>
  <footer>
    <?php require 'footer.php'; ?>
  </footer>
</html>
