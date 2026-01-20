@echo off
setlocal enabledelayedexpansion

:: Define paths
set "PROJECT_ROOT=%~dp0"
set "TARGET_DIR=%PROJECT_ROOT%launchers\packaged"
set "LIB_DIR=%TARGET_DIR%\lib"

echo ===========================================
echo JScience Project Packager
echo ===========================================

:: 1. Build the project
echo [1/4] Building project...
call mvn clean install -DskipTests -f "%PROJECT_ROOT%pom.xml"
if %ERRORLEVEL% NEQ 0 (
    echo Build failed.
    exit /b %ERRORLEVEL%
)

:: 2. Prepare directories
echo [2/4] Preparing directories...
if exist "%TARGET_DIR%" (
    rmdir /s /q "%TARGET_DIR%"
)
mkdir "%LIB_DIR%"

:: 3. Copy dependencies
echo [3/4] Copying dependencies...
call mvn dependency:copy-dependencies -DoutputDirectory="%LIB_DIR%" -DincludeScope=runtime -DskipTests -f "%PROJECT_ROOT%pom.xml"
if %ERRORLEVEL% NEQ 0 (
    echo Copying dependencies failed.
    exit /b %ERRORLEVEL%
)

:: 4. Copy project jars
echo [4/4] Copying project jars...
set modules=jscience-core jscience-natural jscience-social jscience-benchmarks jscience-featured-apps jscience-jni jscience-server jscience-worker jscience-client
for %%m in (%modules%) do (
    if exist "%PROJECT_ROOT%%%m\target\*.jar" (
        copy /y "%PROJECT_ROOT%%%m\target\*.jar" "%LIB_DIR%" >nul
    )
)

:: 5. Create Launch Scripts
echo [5/4] Creating launch scripts...
set "MAIN_CLASS=org.jscience.ui.JScienceMasterControl"

:: run.bat
(
echo @echo off
echo setlocal
echo set "CP=lib\*"
echo java -cp "%%CP%%" %MAIN_CLASS%
echo endlocal
) > "%TARGET_DIR%\run.bat"

:: run.sh
(
echo #!/bin/bash
echo CP="lib/*"
echo java -cp "$CP" %MAIN_CLASS%
) > "%TARGET_DIR%\run.sh"

echo Packaging complete at %TARGET_DIR%
echo ===========================================
endlocal
