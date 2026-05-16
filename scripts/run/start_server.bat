@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

REM Start the Episteme Distributed Server
echo Starting Episteme Server...

REM Check if Maven build is needed
if not exist "episteme-server\target\episteme-server-1.0.0-beta1-exec.jar" (
    echo Building Server...
    call mvn clean package -pl episteme-server -am -DskipTests
)

REM Run Server
java --add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -jar episteme-server\target\episteme-server-1.0.0-beta1-exec.jar

endlocal

