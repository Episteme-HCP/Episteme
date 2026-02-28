<?php
$ip = $_GET['ip'];

function get_country($ip)
{
    $f = fsockopen('ip-to-country.webhosting.info', 80);
    if (!$f)
    {
        return false;
    }

    $postdata = "ip_address=".urlencode($ip)."&submit=".urlencode('Find Country');


    $request = '';
    $request .= "POST /node/view/36 HTTP/1.1\r\n";
    $request .= "Host: ip-to-country.webhosting.info\r\n";
    $request .= "User-Agent: Its me again\r\n";
    $request .= "Content-Length: ".strlen($postdata)."\r\n";
    $request .= "Content-Type: application/x-www-form-urlencoded\r\n";
    $request .= "\r\n";
    $request .= "$postdata\r\n";
    
    fwrite($f, $request);
    $response = '';
    while (!feof($f)) 
    {
           $response .= fgets($f, 128);
    }

    $pos1 = strpos ( $response , '</from>');

    $pos2 = strpos ( $response , '<br><br><img' , $pos1 );

    $parse_from = substr( $response, $pos1+21, ($pos2-$pos1) );
    $pattern = "/<b>([^\/]*)<\/b>/si";
    preg_match_all($pattern, $parse_from, $matches);

    return $matches[1][1];
}

echo (get_country($ip));
 ?>
