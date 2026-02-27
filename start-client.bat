@echo off
setlocal

:: Determine the path to the jscience-core classes
set MODULE_PATH=jscience-core\target\classes;jscience-featured-apps\target\classes;jscience-benchmarks\target\classes
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

echo Launching JScience Studio...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.jscience.core -m org.jscience.core.ui.JScienceDemosApp --monitor %*

endlocal
