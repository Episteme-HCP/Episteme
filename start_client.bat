@echo off
setlocal

:: Determine the path to the episteme-core classes
set MODULE_PATH=episteme-core\target\classes;episteme-featured-apps\target\classes;episteme-benchmarks\target\classes
rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\Episteme-Native"
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

echo Launching Episteme Studio...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.episteme.core -m org.episteme.core.ui.EpistemeDemosApp --monitor %*

endlocal
