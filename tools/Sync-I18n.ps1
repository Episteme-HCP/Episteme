# I18n Sync Script for JScience
# Synchronizes all missing keys across language files

$modules = @(
    @{Path="jscience-core\src\main\resources\org\jscience\ui\i18n"; Prefix="messages_core"},
    @{Path="jscience-natural\src\main\resources\org\jscience\ui\i18n"; Prefix="messages_natural"},
    @{Path="jscience-social\src\main\resources\org\jscience\ui\i18n"; Prefix="messages_social"},
    @{Path="jscience-featured-apps\src\main\resources\org\jscience\apps\i18n"; Prefix="messages_apps"},
    @{Path="jscience-featured-apps\src\main\resources\org\jscience\apps\i18n"; Prefix="messages"}
)

$languages = @("en", "fr", "de", "es", "zh")

function Get-PropertiesKeys {
    param([string]$FilePath)
    $keys = @{}
    if (Test-Path $FilePath) {
        Get-Content $FilePath -Encoding UTF8 | ForEach-Object {
            if ($_ -notmatch '^\s*#' -and $_ -match '^([^=]+)=(.*)$') {
                $keys[$Matches[1].Trim()] = $Matches[2]
            }
        }
    }
    return $keys
}

function Sync-Module {
    param([string]$BasePath, [string]$Prefix)
    
    Write-Host "`n=== Syncing $Prefix ===" -ForegroundColor Cyan
    
    # Load all language files
    $allData = @{}
    $allKeys = @()
    foreach ($lang in $languages) {
        $file = Join-Path $BasePath "$($Prefix)_$($lang).properties"
        $allData[$lang] = Get-PropertiesKeys $file
        $allKeys += $allData[$lang].Keys
    }
    
    # Get unique keys
    $uniqueKeys = $allKeys | Sort-Object -Unique
    Write-Host "Total unique keys: $($uniqueKeys.Count)"
    
    # For each language, find and add missing keys
    foreach ($lang in $languages) {
        $file = Join-Path $BasePath "$($Prefix)_$($lang).properties"
        $currentKeys = $allData[$lang].Keys
        $missingKeys = $uniqueKeys | Where-Object { $_ -notin $currentKeys }
        
        if ($missingKeys.Count -gt 0) {
            Write-Host "  $lang : Adding $($missingKeys.Count) missing keys" -ForegroundColor Yellow
            
            # Find values from other languages (prefer EN, then FR)
            $toAdd = @()
            foreach ($key in $missingKeys) {
                $value = $null
                foreach ($srcLang in @("en", "fr", "de", "es", "zh")) {
                    if ($allData[$srcLang].ContainsKey($key)) {
                        $value = $allData[$srcLang][$key]
                        break
                    }
                }
                if ($value) {
                    $toAdd += "# TODO: Translate from source"
                    $toAdd += "$key=$value"
                } else {
                    $toAdd += "$key=!$key!"
                }
            }
            
            # Append to file
            Add-Content -Path $file -Value "`n# === Auto-synced $($missingKeys.Count) missing keys ===" -Encoding UTF8
            Add-Content -Path $file -Value ($toAdd -join "`n") -Encoding UTF8
        } else {
            Write-Host "  $lang : Complete (no missing keys)" -ForegroundColor Green
        }
    }
}

# Run sync for all modules
foreach ($module in $modules) {
    if (Test-Path $module.Path) {
        Sync-Module -BasePath $module.Path -Prefix $module.Prefix
    } else {
        Write-Host "Path not found: $($module.Path)" -ForegroundColor Red
    }
}

Write-Host "`nSync complete!" -ForegroundColor Green
