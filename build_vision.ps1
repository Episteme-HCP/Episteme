# Script to compile episteme_vision.dll via CMake and copy it to the test resources directory

$buildDir = "episteme-native\build_vision"
$sourceDir = "episteme-native\src\main\cpp"
$outputDir = "episteme-native\src\main\resources\win32-x86_64"

if (Test-Path $buildDir) { Remove-Item -Recurse -Force $buildDir }
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
Set-Location $buildDir

Write-Host "Configuring CMake..."
$absSourceDir = Join-Path $PSScriptRoot "episteme-native\src\main\cpp"
cmake $absSourceDir -G "Visual Studio 18 2026" -A x64

Write-Host "Building Release..."
cmake --build . --config Release

Set-Location "../../.."

if (!(Test-Path $outputDir)) { New-Item -ItemType Directory -Force -Path $outputDir | Out-Null }
Write-Host "Copying episte_vision.dll to resources..."
Copy-Item "$buildDir\Release\episteme_vision.dll" -Destination "$outputDir\episteme_vision.dll" -Force

Write-Host "Done!"
