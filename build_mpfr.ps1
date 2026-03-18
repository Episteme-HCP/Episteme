# Script to compile mpfr.dll via CMake and copy it to the libs directory
# Uses mini-gmp to avoid full GMP dependency

$buildDir = "episteme-native\src\main\cpp\mpfr_build\build"

if (Test-Path $buildDir) { Remove-Item -Recurse -Force $buildDir -ErrorAction SilentlyContinue }
if (!(Test-Path $buildDir)) { New-Item -ItemType Directory -Force -Path $buildDir | Out-Null }
Set-Location $buildDir

Write-Host "Configuring CMake for MPFR..."
$vcvars = "C:\Program Files\Microsoft Visual Studio\18\Community\VC\Auxiliary\Build\vcvarsall.bat"
$absSourceDir = ".."

cmd /c "`"$vcvars`" x64 && cmake `"$absSourceDir`" -G `"Visual Studio 18 2026`" -A x64"

Write-Host "Building MPFR Release..."
cmd /c "`"$vcvars`" x64 && cmake --build . --config Release"

Set-Location $PSScriptRoot

Write-Host "Installing MPFR DLL to libs..."
cmd /c "`"$vcvars`" x64 && cmake --install episteme-native\src\main\cpp\mpfr_build\build"

Write-Host "Done! mpfr.dll should be in the libs directory."
