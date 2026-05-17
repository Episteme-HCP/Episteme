# scripts/setup/setup_launchers.ps1
# Setup launcher libraries by copying JavaFX and dependencies from target directory

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..")

Write-Host "=== Episteme Launcher Libraries Setup ===" -ForegroundColor Cyan
Write-Host "Project Root: $ProjectRoot" -ForegroundColor Yellow

$FeaturedAppsDir = Join-Path $ProjectRoot "episteme-featured-apps"
$LaunchersDir = Join-Path $FeaturedAppsDir "launchers"
$LaunchersLibsDir = Join-Path $LaunchersDir "libs"
$LaunchersLibsSubDir = Join-Path $LaunchersLibsDir "libs"
$LaunchersJavaFXDir = Join-Path $LaunchersLibsDir "javafx"

# Check if featured-apps target/lib exists. If not, trigger/ask for build.
$TargetLibDir = Join-Path $FeaturedAppsDir "target\lib"
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

# Create directories
Write-Host "Creating target folders..." -ForegroundColor Green
$Dirs = @($LaunchersLibsDir, $LaunchersLibsSubDir, $LaunchersJavaFXDir)
foreach ($dir in $Dirs) {
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
    Write-Host "  Copied: $($jar.Name)"
}

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
    "episteme-social"
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
            Write-Host "  Copied $($srcJar.Name) to libs"
            
            # Also copy/rename as the SNAPSHOT name expected by launchers
            $snapshotName = "$mod-1.0.0-SNAPSHOT.jar"
            $destSnapshot = Join-Path $LaunchersLibsDir $snapshotName
            Copy-Item -Path $srcJar.FullName -Destination $destSnapshot -Force
            Write-Host "  Created SNAPSHOT alias: $snapshotName"
        } else {
            Write-Warning "No built jar found in $modTarget"
        }
    } else {
        Write-Warning "Target folder not found for module $mod"
    }
}

Write-Host "Setup complete! Launcher libraries are fully populated." -ForegroundColor Green
