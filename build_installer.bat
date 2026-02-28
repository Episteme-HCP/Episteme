@echo off
echo ===========================================
echo Episteme Suite Installer Builder
echo ===========================================

echo [1/2] Building Runnable Fat JAR...
call mvn clean package -pl episteme-featured-apps -am -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build failed.
    exit /b %ERRORLEVEL%
)

echo [2/2] Generating Native Installer (App Image)...
jpackage --name "EpistemeSuite" ^
  --input episteme-featured-apps/target ^
  --main-jar episteme-featured-apps-1.0.0-SNAPSHOT.jar ^
  --main-class org.episteme.apps.Launcher ^
  --type app-image ^
  --dest installer ^
  --description "Episteme Demos and Featured Apps" ^
  --vendor "Silvere Martin-Michiellot"

echo ===========================================
echo Installer generated in: %~dp0installer\EpistemeSuite
echo ===========================================
pause
