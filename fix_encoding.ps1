$path = "c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\resources\org\jscience\ui\i18n\messages_natural_en.properties"
try {
    $content = Get-Content $path -Raw
    $content | Set-Content $path -Encoding UTF8
    Write-Host "Fixed encoding to UTF8"
} catch {
    Write-Host "Error reading file: $_"
}
