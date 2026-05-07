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

REM Start a Episteme Worker Node
echo Starting Episteme Worker Node...

if not exist "episteme-worker\target\episteme-worker-1.0.0-SNAPSHOT.jar" (
    echo Building Worker...
    call mvn clean package -pl episteme-worker -am -DskipTests
)

echo Launching Worker connected to localhost:50051...
set PORT=%1
if "%PORT%"=="" set PORT=50051

java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp episteme-worker\target\episteme-worker-1.0.0-SNAPSHOT.jar;episteme-worker\target\lib\* org.episteme.worker.WorkerNode localhost %PORT%
