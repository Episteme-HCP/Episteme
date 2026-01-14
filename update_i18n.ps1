param (
    [string]$ModulePath = "jscience-core",
    [string]$SourceLang = "en",
    [string[]]$TargetLangs = @("fr", "de", "es", "zh")
)

function Load-Properties {
    param ([string]$Path)
    $props = [ordered]@{}
    if (Test-Path $Path) {
        $content = Get-Content $Path -Encoding UTF8
        foreach ($line in $content) {
            if ($line -match "^([^#=]+)=(.*)$") {
                $key = $matches[1].Trim()
                $value = $matches[2].Trim()
                if (-not $props.Contains($key)) {
                    $props[$key] = $value
                }
            }
        }
    }
    return $props
}

$searchPath = "$ModulePath\src\main\resources"
if (-not (Test-Path $searchPath)) {
    Write-Host "Chemin de module introuvable: $searchPath" -ForegroundColor Red
    exit 1
}

$resourcesDir = Get-ChildItem -Path $searchPath -Recurse -Directory | Where-Object { $_.FullName -match "i18n" } | Select-Object -First 1

if (-not $resourcesDir) {
    Write-Host "Dossier i18n introuvable dans $ModulePath" -ForegroundColor Red
    exit 1
}

Write-Host "Dossier: $($resourcesDir.FullName)" -ForegroundColor Gray

# Charger les sources
$pattern = "*_$SourceLang.properties"
$sourceFileItems = Get-ChildItem -Path $resourcesDir.FullName -Filter $pattern

if ($sourceFileItems.Count -eq 0) {
    Write-Host "Aucun fichier source trouvé ($pattern) dans $($resourcesDir.FullName)" -ForegroundColor Red
    exit 1
}

foreach ($item in $sourceFileItems) {
    $sourceName = $item.Name
    $sourceFile = $item.FullName
    
    # Skip if it is a target file (just in case SourceLang is one of TargetLangs, unlikely but specific check avoided)
    
    Write-Host "Source: $sourceName" -ForegroundColor Green
    $sourceProps = Load-Properties -Path $sourceFile
    
    foreach ($lang in $TargetLangs) {
        $targetName = $sourceName.Replace("_$SourceLang.properties", "_$lang.properties")
        $targetPath = Join-Path $resourcesDir.FullName $targetName
        
        if (-not (Test-Path $targetPath)) {
            Write-Host "  + Cration de $targetName" -ForegroundColor Cyan
            New-Item -Path $targetPath -ItemType File -Force | Out-Null
        }
        
        Write-Host "  > Vrification $lang..." -NoNewline
        
        $targetProps = Load-Properties -Path $targetPath
        $missingKeys = @()
        
        foreach ($key in $sourceProps.Keys) {
            if (-not $targetProps.Contains($key)) {
                $missingKeys += $key
            }
        }
        
        if ($missingKeys.Count -gt 0) {
            Write-Host " $($missingKeys.Count) manquantes" -ForegroundColor Yellow
            Add-Content -Path $targetPath -Value "`n# === Auto-synced $($missingKeys.Count) missing keys ===" -Encoding UTF8
            
            foreach ($key in $missingKeys) {
                $val = $sourceProps[$key]
                # Simple escape for newlines
                $escapedVal = $val.Replace("`n", "\n").Replace("`r", "")
                Add-Content -Path $targetPath -Value "# TODO: Translate`n$key=[MISSING] $escapedVal" -Encoding UTF8
            }
        } else {
            Write-Host " OK" -ForegroundColor Green
        }
    }
    Write-Host ""
}
