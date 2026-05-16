# build_miniaudio.ps1
# Compiles miniaudio.dll and copies it to the root libs/ directory.

$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Definition
$buildDir = Join-Path $scriptPath "episteme-native/build_miniaudio"
$sourceDir = Join-Path $scriptPath "episteme-native/src/main/cpp/audio"
$outputDir = Join-Path $scriptPath "libs"

if (Test-Path $buildDir) { Remove-Item -Recurse -Force $buildDir }
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
Set-Location $buildDir

Write-Host "Configuring CMake for miniaudio..."
cmake "$sourceDir" -G "Visual Studio 18 2026" -A x64

Write-Host "Building miniaudio (Release)..."
cmake --build . --config Release

if (!(Test-Path $outputDir)) { New-Item -ItemType Directory -Force -Path $outputDir | Out-Null }
Write-Host "Consolidating miniaudio.dll to libs/..."
Copy-Item "Release\miniaudio.dll" -Destination "$outputDir\miniaudio.dll" -Force

Set-Location $scriptPath
Write-Host "[SUCCESS] miniaudio compilation complete."
