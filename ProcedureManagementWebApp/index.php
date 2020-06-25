<?php session_start(); ?>
<!DOCTYPE html>
<html lang="de">
  <head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex,nofollow">
    <title>Ablauf Hilfe | Login</title>
    <link rel="stylesheet" href="style.css">
    <script src="scripts.js"></script>
  </head>
  <header>
    <div class="container">
      <img src="logo.png" alt="Ablauf Hilfe Logo">
    </div>
  </header>
  <body>
    <?php require 'amazon_login_script.html'; ?>
    <div class="container">
      <a href id="LoginWithAmazon">
        <img border="0" alt="Login with Amazon" src="https://images-na.ssl-images-amazon.com/images/G/01/lwa/btnLWA_gold_390x92.png" width="180"/>
      </a>
      <script type="text/javascript">
        document.getElementById('LoginWithAmazon').onclick = function() {
          options = {}
          options.scope = 'profile';
          options.scope_data = {
            'profile' : {'essential': false}
          };
          amazon.Login.authorize(options, 'https://ablaufhilfe.stvs.me/handle_login.php');
          return false;
        };
      </script>
    </div>
  </body>
  <footer>
    <?php require 'footer.php'; ?>
  </footer>
</html>
