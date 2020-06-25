<?php
// verify that the access token belongs to us
$c = curl_init('https://api.amazon.com/auth/o2/tokeninfo?access_token=' . urlencode($_REQUEST['access_token']));
curl_setopt($c, CURLOPT_RETURNTRANSFER, true);

$r = curl_exec($c);
curl_close($c);
$d = json_decode($r);

if ($d->aud != 'amzn1.application-oa2-client.9326e5295f95408ea7c02baee0c06e2c') {
  // the access token does not belong to us
  header('HTTP/1.1 404 Not Found');
  echo 'Page not found';
  exit;
}

// exchange the access token for user profile
$c = curl_init('https://api.amazon.com/user/profile');
curl_setopt($c, CURLOPT_HTTPHEADER, array('Authorization: bearer ' . $_REQUEST['access_token']));
curl_setopt($c, CURLOPT_RETURNTRANSFER, true);

$r = curl_exec($c);
curl_close($c);
$d = json_decode($r);

session_start();
$_SESSION["userId"] = $d->user_id;
$_SESSION["userName"] = $d->name;

header('Location: dashboard.php');
exit;

?>
