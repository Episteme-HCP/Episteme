
$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
# Script is in ROOT
$projectRoot = $scriptDir
$launchersDir = Join-Path $projectRoot "launchers"
$targetDir = Join-Path $launchersDir "packaged"

Write-Host "Building project from $projectRoot ..."
Set-Location $projectRoot
# Build all modules
mvn clean install -DskipTests

Write-Host "Cleaning target directory: $targetDir"
if (Test-Path $targetDir) { Remove-Item -Recurse -Force $targetDir }
New-Item -ItemType Directory -Force $targetDir | Out-Null

Write-Host "Copying artifacts (Thin Jars only)..."
# All project modules
$modules = @(
    "episteme-core", 
    "episteme-natural", 
    "episteme-social", 
    "episteme-native", 
    "episteme-featured-apps", 
    "episteme-server",
    "episteme-client",
    "episteme-database",
    "episteme-worker",
    "episteme-benchmarks",
    "episteme-jni"
)

foreach ($mod in $modules) {
    $modTarget = Join-Path $projectRoot "$mod\target"
    if (Test-Path $modTarget) {
        # Find jars. 
        # Strategy: 
        # 1. Search for original-X.jar (the thin jar created by shade plugin)
        # 2. If not found, search for X.jar (making sure it's not a shaded/sources/javadoc version)
        
        $allJars = Get-ChildItem $modTarget -Filter "*.jar" | Where-Object { 
            ($_.Name -match "^original-$mod-.*\.jar$") -or ($_.Name -match "^$mod-.*\.jar$") 
        } | Where-Object { 
            $_.Name -notmatch "-sources" -and $_.Name -notmatch "-javadoc" -and $_.Name -notmatch "-shaded" 
        }

        # Priority to 'original-'
        $original = $allJars | Where-Object { $_.Name -match "^original-" } | Select-Object -First 1
        
        if ($original) {
            $destName = $original.Name -replace "^original-", ""
            Write-Host "Copying $($original.Name) as $destName (Thin)"
            Copy-Item $original.FullName -Destination (Join-Path $targetDir $destName)
        } else {
            # Find the true thin jar (usually the shortest name or exactly $mod-version.jar)
            $thin = $allJars | Sort-Object { $_.Name.Length } | Select-Object -First 1
            if ($thin) {
                 Write-Host "Copying $($thin.Name) (Thin)"
                 Copy-Item $thin.FullName -Destination $targetDir
            } else {
                Write-Warning "No valid thin jar found for $mod"
            }
        }
    } else {
        Write-Warning "Module target not found: $mod"
    }
}

Write-Host "Packaging complete. Handled thin versions for featured-apps and worker."
Write-Host "Launchers and side-libs are assumed to be in $launchersDir."
Write-Host "Files in $targetDir"
