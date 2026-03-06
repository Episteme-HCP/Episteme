# Script to compile episteme-jni.dll via MSVC cl.exe and copy it to the module libs directory
# Requires Visual Studio Developer Command Prompt environment

if (-not (Test-Path Env:\JAVA_HOME)) {
    Write-Error "ERROR: JAVA_HOME is not set."
    exit 1
}

$java_home = $Env:JAVA_HOME
$sourceDir = "episteme-jni\src\main\cpp"
$outputDir = "episteme-jni\libs"

if (!(Test-Path $outputDir)) { New-Item -ItemType Directory -Force -Path $outputDir | Out-Null }

Write-Host "Compiling episteme-jni for Windows..."

# Use cl.exe to compile JNI C++ code into a DLL
cl.exe /O2 /LD /EHsc `
    /I"`"$java_home\include`"" `
    /I"`"$java_home\include\win32`"" `
    /I"`"$sourceDir`"" `
    "$sourceDir\episteme_jni.cpp" `
    /link /OUT:"$outputDir\episteme-jni.dll"

if ($LASTEXITCODE -eq 0) {
    Write-Host "[SUCCESS] episteme-jni.dll created in $outputDir"
    # Clean up MSVC build artifacts from current directory
    if (Test-Path "episteme_jni.obj") { Remove-Item "episteme_jni.obj" }
    if (Test-Path "$outputDir\episteme-jni.exp") { Remove-Item "$outputDir\episteme-jni.exp" }
    if (Test-Path "$outputDir\episteme-jni.lib") { Remove-Item "$outputDir\episteme-jni.lib" }
} else {
    Write-Error "[FAILURE] Compilation failed"
    exit 1
}
