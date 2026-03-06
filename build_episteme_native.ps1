# Script to compile episteme-native.dll via CMake and copy it to the libs directory

$buildDir = "episteme-native\build_vision"
$sourceDir = "episteme-native\src\main\cpp"
$outputDir = "episteme-native\libs"

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
Write-Host "Copying episteme-native.dll to libs..."
Copy-Item "$buildDir\Release\episteme-native.dll" -Destination "$outputDir\episteme-native.dll" -Force

Write-Host "Done!"
