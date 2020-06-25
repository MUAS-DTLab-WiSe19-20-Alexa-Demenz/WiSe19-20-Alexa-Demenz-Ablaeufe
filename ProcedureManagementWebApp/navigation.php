<div class="container">
  <img src="logo.png" alt="Ablauf Hilfe Logo">
  <nav>
    <ul>
      <li<?php if (basename($_SERVER['SCRIPT_FILENAME']) === 'dashboard.php') echo ' class="current"'; ?>><a href="dashboard.php">Dashboard</a></li>
      <li<?php if (basename($_SERVER['SCRIPT_FILENAME']) === 'procedure-editor.php') echo ' class="current"'; ?>><a href="procedure-editor.php">Ablauf-Editor</a></li>
    </ul>
  </nav>
</div>
