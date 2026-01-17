# Update messages_client_en.properties with new I18n key nomenclature
$file = "c:\Silvere\Encours\Developpement\JScience\jscience-client\src\main\resources\org\jscience\client\ui\i18n\messages_client_en.properties"
$content = Get-Content $file -Raw -Encoding UTF8

# Define new keys to add/update
$newKeys = @"

# Updated keys for DistributedDataLakeApp
app.distributeddatalakeapp.btn.stream_stars=⭐ Stream Stars
app.distributeddatalakeapp.btn.clear=Clear
app.distributeddatalakeapp.lbl.output=Streaming Data Output
app.distributeddatalakeapp.throughput.default=Throughput: 0 items/s
app.distributeddatalakeapp.msg.chunk_format=[{0}] Position: {1}, Sequence: {2}...
app.distributeddatalakeapp.throughput.chunks=Throughput: {0} chunks/s
app.distributeddatalakeapp.throughput.stars=Throughput: {0} stars/s
app.distributeddatalakeapp.status.error_short=Error
app.distributeddatalakeapp.btn.genome=🧬 Stream Genome
"@

# Check if keys don't exist yet, then add them
if ($content -notmatch "app\.distributeddatalakeapp\.btn\.stream_stars=") {
    $content = $content + $newKeys
    Set-Content $file $content -Encoding UTF8
    Write-Host "Added new DistributedDataLakeApp keys to properties file"
} else {
    Write-Host "Keys already exist, skipping"
}
