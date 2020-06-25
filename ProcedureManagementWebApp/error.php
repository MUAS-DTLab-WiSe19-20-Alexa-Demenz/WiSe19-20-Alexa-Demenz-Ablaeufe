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
    <title>Ablauf Hilfe | Fehler</title>
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
  </head>
  <header>
    <?php require 'navigation.php'; ?>
  </header>
  <body>
    <?php require 'amazon_login_script.html'; ?>
    <div class="container">
      <h3 style="text-align: center;">Es ist ein Fehler aufgetreten!</h3>
      <p><?php if($_SESSION["errorMessage"] != null) echo $_SESSION["errorMessage"]; ?></p>
    </div>
  </body>
  <footer>
    <?php require 'footer.php'; ?>
  </footer>
</html>
