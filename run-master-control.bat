@echo off

rem --- Native Libraries Setup ---
if exist "C:\Program Files\VideoLAN\VLC" (
    echo [INFO] Adding VLC to PATH...
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)
if exist "%~dp0libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0libs;%PATH%"
)
if exist "%~dp0..\libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0..\libs;%PATH%"
)

setlocal

echo Launching Episteme Master Control via Maven...
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
call mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.core.ui.EpistemeMasterControl" -Dexec.args="%*"

endlocal
