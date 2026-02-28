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

REM Start the Episteme Distributed Server
echo Starting Episteme Server...

REM Check if Maven build is needed (simplified check)
if not exist "episteme-server\target\episteme-server-1.0.0-SNAPSHOT.jar" (
    echo Building Server...
    call mvn clean package -pl episteme-server -am -DskipTests
)

REM Run Server
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp episteme-server\target\episteme-server-1.0.0-SNAPSHOT.jar;episteme-server\target\lib\* org.episteme.server.JscienceServer
