
$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
# Script is in ROOT
$projectRoot = $scriptDir
$launchersDir = Join-Path $projectRoot "launchers"
$targetDir = Join-Path $launchersDir "packaged"
$libDir = Join-Path $targetDir "lib"

Write-Host "Building project from $projectRoot ..."
Set-Location $projectRoot
# Ensure jars are built
mvn clean install -DskipTests

Write-Host "Cleaning target directory: $targetDir"
if (Test-Path $targetDir) { Remove-Item -Recurse -Force $targetDir }
New-Item -ItemType Directory -Force $targetDir | Out-Null
New-Item -ItemType Directory -Force $libDir | Out-Null

Write-Host "Copying dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory="$libDir" -DincludeScope=runtime -DskipTests

Write-Host "Copying project artifacts..."
$modules = @("jscience-core", "jscience-natural", "jscience-social", "jscience-native", "jscience-featured-apps", "jscience-server")
foreach ($mod in $modules) {
    $jarPath = Join-Path $projectRoot "$mod\target"
    if (Test-Path $jarPath) {
        Get-ChildItem $jarPath -Filter "$mod-*.jar" | Where-Object { $_.Name -notmatch "sources" -and $_.Name -notmatch "javadoc" } | ForEach-Object {
            Write-Host "Copying $($_.Name)"
            Copy-Item $_.FullName -Destination $targetDir
        }
    }
}

Write-Host "Generating launchers (Windows)..."
if (Test-Path $launchersDir) {
    $sourceLaunchers = Get-ChildItem $launchersDir -Filter "run-*.bat"

    foreach ($launcher in $sourceLaunchers) {
        $content = Get-Content $launcher.FullName
        $appClassLine = $content | Where-Object { $_ -match "set APP_CLASS=" }
        $appClass = $null
        if ($appClassLine) {
            $appClass = $appClassLine -replace "set APP_CLASS=", ""
        }
    
        if (-not $appClass) {
            Write-Warning "Skipping $($launcher.Name): APP_CLASS not found"
            continue
        }

        Write-Host "Processing $($launcher.Name) -> $appClass"

        $newContent = @"
@echo off
setlocal

set APP_CLASS=$appClass
set LIB_DIR=lib
set NATIVE_ARGS=--enable-native-access=ALL-UNNAMED --enable-preview
set CLASSPATH=.;*;lib\*

echo Starting $appClass ...
java %NATIVE_ARGS% -cp "%CLASSPATH%" %APP_CLASS%

endlocal
"@

        $newFile = Join-Path $targetDir $launcher.Name
        Set-Content -Path $newFile -Value $newContent
    }
} else {
    Write-Warning "Launchers directory not found: $launchersDir"
}

Write-Host "Done. Packages available in $targetDir"
