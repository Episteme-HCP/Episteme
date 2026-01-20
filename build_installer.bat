@echo off
echo ===========================================
echo JScience Suite Installer Builder
echo ===========================================

echo [1/2] Building Runnable Fat JAR...
call mvn clean package -pl jscience-featured-apps -am -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build failed.
    exit /b %ERRORLEVEL%
)

echo [2/2] Generating Native Installer (App Image)...
jpackage --name "JScienceSuite" ^
  --input jscience-featured-apps/target ^
  --main-jar jscience-featured-apps-1.0.0-SNAPSHOT.jar ^
  --main-class org.jscience.apps.Launcher ^
  --type app-image ^
  --dest installer ^
  --description "JScience Demos and Featured Apps" ^
  --vendor "Silvere Martin-Michiellot"

echo ===========================================
echo Installer generated in: %~dp0installer\JScienceSuite
echo ===========================================
pause
