# scripts/setup/setup_launchers.ps1
# Setup launcher libraries by copying JavaFX and dependencies from target directory

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..")

Write-Host "=== Episteme Launcher Libraries Setup ===" -ForegroundColor Cyan
Write-Host "Project Root: $ProjectRoot" -ForegroundColor Yellow

$FeaturedAppsDir = Join-Path $ProjectRoot "episteme-featured-apps"
$TargetLibDir = Join-Path $FeaturedAppsDir "target\lib"

# Check if featured-apps target/lib exists. If not, trigger build.
if (!(Test-Path $TargetLibDir)) {
    Write-Host "[WARNING] Target dependencies not found at $TargetLibDir" -ForegroundColor Yellow
    Write-Host "Please build the project first using 'mvn clean package -DskipTests' or run it now." -ForegroundColor Yellow
    
    # Try to build
    Write-Host "Running Maven package build to generate dependencies..." -ForegroundColor Cyan
    Set-Location $ProjectRoot
    mvn clean package -pl episteme-featured-apps -am -DskipTests
    
    if (!(Test-Path $TargetLibDir)) {
        Write-Error "Maven build failed to generate dependencies directory at $TargetLibDir"
    }
}

# --- 1. Populate episteme-featured-apps/launchers/libs (for Multi-Launcher apps) ---
Write-Host "`n--- Populating Multi-Launcher Libraries ---" -ForegroundColor Cyan
$LaunchersDir = Join-Path $FeaturedAppsDir "launchers"
$LaunchersLibsDir = Join-Path $LaunchersDir "libs"
$LaunchersLibsSubDir = Join-Path $LaunchersLibsDir "libs"
$LaunchersJavaFXDir = Join-Path $LaunchersLibsDir "javafx"

$Dirs1 = @($LaunchersLibsDir, $LaunchersLibsSubDir, $LaunchersJavaFXDir)
foreach ($dir in $Dirs1) {
    if (!(Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "  Created: $dir"
    }
}

# Copy JavaFX Jars
Write-Host "Copying JavaFX Jars to $LaunchersJavaFXDir..." -ForegroundColor Green
$JavaFXJars = Get-ChildItem -Path $TargetLibDir -Filter "javafx-*"
foreach ($jar in $JavaFXJars) {
    Copy-Item -Path $jar.FullName -Destination $LaunchersJavaFXDir -Force
}
Write-Host "  Copied JavaFX Jars."

# Copy other Jars to libs/libs
Write-Host "Copying dependencies to $LaunchersLibsSubDir..." -ForegroundColor Green
$DepJars = Get-ChildItem -Path $TargetLibDir -Filter "*.jar" | Where-Object { $_.Name -notlike "javafx-*" }
foreach ($jar in $DepJars) {
    Copy-Item -Path $jar.FullName -Destination $LaunchersLibsSubDir -Force
}
Write-Host "  Copied $($DepJars.Count) dependencies."

# Copy project modules jars to libs as SNAPSHOT
Write-Host "Copying Episteme modules Jars..." -ForegroundColor Green
$Modules = @(
    "episteme-featured-apps",
    "episteme-core",
    "episteme-natural",
    "episteme-social",
    "episteme-demos"
)

foreach ($mod in $Modules) {
    $modTarget = Join-Path $ProjectRoot "$mod\target"
    if (Test-Path $modTarget) {
        $jars = Get-ChildItem -Path $modTarget -Filter "$mod-*.jar" | Where-Object {
            $_.Name -notmatch "-sources" -and $_.Name -notmatch "-javadoc" -and $_.Name -notmatch "-shaded"
        }
        if ($jars.Count -gt 0) {
            $srcJar = $jars[0]
            # Copy as the original name
            Copy-Item -Path $srcJar.FullName -Destination $LaunchersLibsDir -Force
            
            # Also copy/rename as the SNAPSHOT name expected by launchers
            $snapshotName = "$mod-1.0.0-SNAPSHOT.jar"
            $destSnapshot = Join-Path $LaunchersLibsDir $snapshotName
            Copy-Item -Path $srcJar.FullName -Destination $destSnapshot -Force
        }
    }
}
Write-Host "  Populated Episteme module JARs."

# --- 2. Populate launchers/lib (for root run_demos.bat / run_demos.sh) ---
Write-Host "`n--- Populating stand-alone launchers/lib ---" -ForegroundColor Cyan
$RootLaunchersDir = Join-Path $ProjectRoot "launchers"
$RootLaunchersLibDir = Join-Path $RootLaunchersDir "lib"
$RootLaunchersJavaFXDir = Join-Path $RootLaunchersLibDir "javafx"

$Dirs2 = @($RootLaunchersDir, $RootLaunchersLibDir, $RootLaunchersJavaFXDir)
foreach ($dir in $Dirs2) {
    if (!(Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "  Created: $dir"
    }
}

# Copy JavaFX Jars
Write-Host "Copying JavaFX Jars to $RootLaunchersJavaFXDir..." -ForegroundColor Green
foreach ($jar in $JavaFXJars) {
    Copy-Item -Path $jar.FullName -Destination $RootLaunchersJavaFXDir -Force
}
Write-Host "  Copied JavaFX Jars."

# Copy other Jars to launchers/lib
Write-Host "Copying dependencies directly to $RootLaunchersLibDir..." -ForegroundColor Green
foreach ($jar in $DepJars) {
    Copy-Item -Path $jar.FullName -Destination $RootLaunchersLibDir -Force
}
Write-Host "  Copied $($DepJars.Count) dependencies."

# Copy project modules jars directly to launchers/lib as SNAPSHOT
Write-Host "Copying Episteme modules Jars..." -ForegroundColor Green
foreach ($mod in $Modules) {
    $modTarget = Join-Path $ProjectRoot "$mod\target"
    if (Test-Path $modTarget) {
        $jars = Get-ChildItem -Path $modTarget -Filter "$mod-*.jar" | Where-Object {
            $_.Name -notmatch "-sources" -and $_.Name -notmatch "-javadoc" -and $_.Name -notmatch "-shaded"
        }
        if ($jars.Count -gt 0) {
            $srcJar = $jars[0]
            # Copy as the original name
            Copy-Item -Path $srcJar.FullName -Destination $RootLaunchersLibDir -Force
            
            # Also copy/rename as SNAPSHOT name
            $snapshotName = "$mod-1.0.0-SNAPSHOT.jar"
            $destSnapshot = Join-Path $RootLaunchersLibDir $snapshotName
            Copy-Item -Path $srcJar.FullName -Destination $destSnapshot -Force
        }
    }
}
Write-Host "  Populated Episteme module JARs."

# --- 3. Path correction in batch launchers for development mode classpath ---
Write-Host "`n--- Correcting relative development paths in batch launchers ---" -ForegroundColor Cyan
$BatFiles = Get-ChildItem -Path $LaunchersDir -Filter "*.bat"
foreach ($file in $BatFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    if ($content -like "*%~dp0..\episteme-*") {
        Write-Host "  Correcting paths in: $($file.Name)" -ForegroundColor Yellow
        $newContent = $content -replace "%~dp0\.\.episteme-", "%~dp0..\..\episteme-"
        Set-Content -Path $file.FullName -Value $newContent -Force
    }
}

Write-Host "`nSetup complete! All launcher libraries are fully populated and standalone runs will now work." -ForegroundColor Green
