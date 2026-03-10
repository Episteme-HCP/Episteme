# Script to compile episteme-native.dll via CMake and copy it to the libs directory

$buildDir = "episteme-native\build_native"
$outputDir = "libs"

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

Write-Host "Copying DLLs to libs..."
# Vision library is now in vision/Release
$absBuildRel = Join-Path $PSScriptRoot "$buildDir\vision\Release\episteme-native.dll"
$absDest = Join-Path $PSScriptRoot "$outputDir\episteme-native.dll"
if (Test-Path $absBuildRel) {
    Copy-Item $absBuildRel -Destination $absDest -Force
}

# Audio library is in audio\Release
$absAudioRel = Join-Path $PSScriptRoot "$buildDir\audio\Release\miniaudio.dll"
$absAudioDest = Join-Path $PSScriptRoot "$outputDir\miniaudio.dll"
if (Test-Path $absAudioRel) {
    Copy-Item $absAudioRel -Destination $absAudioDest -Force
}

Write-Host "Done!"
