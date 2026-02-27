@echo off
setlocal
cd /d %~dp0..
set APP_CLASS=org.jscience.apps.apps.sociology.CivilizationApp
set LIB_DIR=launchers\libs\libs
set MODULES_DIR=launchers\libs
set MODULE_PATH=%MODULES_DIR%\jscience-featured-apps-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-core-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-natural-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-social-1.0.0-SNAPSHOT.jar
rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\JScience-Native"
set "LIBS_DIR=%~dp0libs"

rem --- VLC ---
if exist "C:\Program Files\VideoLAN\VLC" (
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)

rem --- Project libs directory ---
if exist "%LIBS_DIR%" (
    set "PATH=%LIBS_DIR%;%PATH%"
)

set PATH=%~dp0..\libs;%PATH%
set PATH=%~dp0..\libs;%PATH%
echo Starting Civilization Collapse Model...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%~dp0libs\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%~dp0..\jscience-featured-apps\target\classes;%~dp0..\jscience-core\target\classes;%~dp0..\jscience-natural\target\classes;%~dp0..\jscience-social\target\classes;%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*

endlocal

