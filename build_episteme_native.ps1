# Script to compile episteme-native.dll via CMake and copy it to the libs directory

$buildDir = "episteme-native\build_vision"
$outputDir = "libs"

if (Test-Path $buildDir) { Remove-Item -Recurse -Force $buildDir }
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
Set-Location $buildDir

Write-Host "Configuring CMake..."
$vcvars = "C:\Program Files\Microsoft Visual Studio\18\Community\VC\Auxiliary\Build\vcvarsall.bat"
$absSourceDir = Join-Path $PSScriptRoot "episteme-native\src\main\cpp"

cmd /c "`"$vcvars`" x64 && cmake `"$absSourceDir`" -G `"Visual Studio 18 2026`" -A x64"

Write-Host "Building Release..."
cmd /c "`"$vcvars`" x64 && cmake --build . --config Release"

Set-Location $PSScriptRoot

if (!(Test-Path $outputDir)) { New-Item -ItemType Directory -Force -Path $outputDir | Out-Null }
Write-Host "Copying episteme-native.dll to libs..."
$absBuildRel = Join-Path $PSScriptRoot "$buildDir\Release\episteme-native.dll"
$absDest = Join-Path $PSScriptRoot "$outputDir\episteme-native.dll"
Copy-Item $absBuildRel -Destination $absDest -Force

Write-Host "Done!"
