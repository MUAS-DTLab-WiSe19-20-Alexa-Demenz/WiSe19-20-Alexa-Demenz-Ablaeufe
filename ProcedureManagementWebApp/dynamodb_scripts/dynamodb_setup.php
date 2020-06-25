<?php
session_start();

require_once($_SERVER['DOCUMENT_ROOT'] . '/aws/aws-autoloader.php');

require_once($_SERVER['DOCUMENT_ROOT'] . '/.aws/credentials.php'); // see comments in $sdk.

date_default_timezone_set('Europe/Berlin');

use Aws\DynamoDb\Exception\DynamoDbException;
use Aws\DynamoDb\Marshaler;

$sdk = new Aws\Sdk([
    /*'profile' => 'ablaufhilfeSWE',*/ // Home directory is unknown/inaccessible for 'profile' declaration to work.
    'region'   => 'eu-west-1',
    'version'  => 'latest',
    'credentials' => [ // Replaces 'profile' declaration. Not ideal to use "require 'credentials.php'".
        'key'    => $aws_access_key_id,
        'secret' => $aws_secret_access_key,
    ],
]);

$dynamodb = $sdk->createDynamoDb();
$marshaler = new Marshaler();

?>
