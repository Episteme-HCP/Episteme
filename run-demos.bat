@echo off
setlocal

set APP_CLASS=org.jscience.core.ui.JScienceDemosApp
set LIB_DIR=launchers\lib
set MODULE_PATH=jscience-featured-apps\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes;jscience-native\target\classes;jscience-client\target\classes;jscience-server\target\classes
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

echo Starting JScience Demos Suite...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*

endlocal
