$path = "c:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\resources\org\jscience\ui\i18n\messages_core_en.properties"
$content = Get-Content $path -Raw

$content = $content -replace 'generated\.network\.no\.network\.backend\.a', 'viewer.networkviewer.error.nobackend'

# Append default keys if not present (simple check)
if (-not ($content -match 'viewer\.networkviewer\.name')) {
    $content += "`nviewer.networkviewer.name=Network Viewer"
}
if (-not ($content -match 'viewer\.networkviewer\.desc')) {
    $content += "`nviewer.networkviewer.desc=Visualizes networks and graphs."
}
if (-not ($content -match 'viewer\.networkviewer\.error\.nobackend')) {
    $content += "`nviewer.networkviewer.error.nobackend=No Network Backend Available (Install GraphStream, JUNG, etc.)"
}

Set-Content $path $content
Write-Host "NetworkViewer properties updated."
