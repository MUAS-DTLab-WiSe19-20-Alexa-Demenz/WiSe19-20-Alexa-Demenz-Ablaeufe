<?php session_start(); ?>
<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex,nofollow">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ablauf Hilfe | Datenschutz</title>
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
  </head>
  <header>
    <?php require 'navigation.php'; ?>
  </header>
  <body>
    <?php require 'amazon_login_script.html'; ?>
    <div class="container">
      <h3>Datenschutzhinweise</h3>
      <p>
        Dieser Service nutzt "Login with Amazon" (LWA) und verarbeitet personenbezogene Daten für die Nuzung des Ablauf Hilfe Alexa-Skills.<br>
        Durch die Verknüpfung und Nutzung von LWA können Daten wie Name, E-Mail Adresse und die Account-ID verarbeitet und gespeichert werden.
      </p>
    </div>
  </body>
  <footer>
    <?php require 'footer.php'; ?>
  </footer>
</html>
