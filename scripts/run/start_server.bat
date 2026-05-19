@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

REM Start the Episteme Distributed Server
echo Starting Episteme Server...

REM Find the exec jar dynamically
set SERVER_JAR=
for %%f in (episteme-server\target\episteme-server-*-exec.jar) do (
    set "SERVER_JAR=%%f"
)

if "%SERVER_JAR%"=="" (
    echo Building Server...
    call mvn clean package -pl episteme-server -am -DskipTests
    for %%f in (episteme-server\target\episteme-server-*-exec.jar) do (
        set "SERVER_JAR=%%f"
    )
)

if "%SERVER_JAR%"=="" (
    echo [ERROR] Failed to find or build Episteme Server Exec JAR.
    exit /b 1
)

echo Found server jar: %SERVER_JAR%

REM Run Server
java --add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -jar "%SERVER_JAR%" --server.port=0 --grpc.server.port=50052

endlocal
