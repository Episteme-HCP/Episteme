# Script to compile episteme-native.dll via CMake and copy it to the libs directory

$buildDir = "episteme-native\build_native"

if (Test-Path $buildDir) { Remove-Item -Recurse -Force $buildDir -ErrorAction SilentlyContinue }
if (!(Test-Path $buildDir)) { New-Item -ItemType Directory -Force -Path $buildDir | Out-Null }
Set-Location $buildDir

Write-Host "Configuring CMake..."
$vcvars = "C:\Program Files\Microsoft Visual Studio\18\Community\VC\Auxiliary\Build\vcvarsall.bat"
$absSourceDir = Join-Path $PSScriptRoot "episteme-native\src\main\cpp"

cmd /c "`"$vcvars`" x64 && cmake `"$absSourceDir`" -G `"Visual Studio 18 2026`" -A x64"

Write-Host "Building Release..."
cmd /c "`"$vcvars`" x64 && cmake --build . --config Release"

Set-Location $PSScriptRoot

Write-Host "Copying DLLs to libs (via install target)..."
cmd /c "`"$vcvars`" x64 && cmake --install ."

# The install target handles both episteme-native and miniaudio

Write-Host "Done!"
