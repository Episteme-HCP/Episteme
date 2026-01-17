$configPath = "c:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\resources\jscience.properties"
$msgPath = "c:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\resources\org\jscience\ui\i18n\messages_core_en.properties"

# Config update
if (Test-Path $configPath) {
    $config = Get-Content $configPath -Raw
    if ($config -notmatch "api.crossref.base") {
        Add-Content -Path $configPath -Value "`n# CrossRef API`napi.crossref.base=https://api.crossref.org/works/"
        Write-Host "Updated jscience.properties"
    }
} else {
    Write-Host "Config file not found: $configPath"
}

# Messages update
if (Test-Path $msgPath) {
    $msg = Get-Content $msgPath -Raw
    $newKeys = @{
        "reader.crossref.name" = "CrossRef Connector"
        "reader.crossref.desc" = "Resolves DOIs to get publication metadata."
        "reader.crossref.longdesc" = "Connects to CrossRef API to resolve DOIs and retrieve detailed bibliography citation including authors, journal, and year."
        "category.bibliography" = "Bibliography"
    }

    foreach ($key in $newKeys.Keys) {
        if ($msg -notmatch [regex]::Escape($key) + "=") {
            $msg += "`n$key=" + $newKeys[$key]
            Write-Host "Added $key"
        }
    }
    Set-Content -Path $msgPath -Value $msg
    Write-Host "Updated messages_core_en.properties"
} else {
    Write-Host "Messages file not found: $msgPath"
}
