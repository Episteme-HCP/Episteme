@echo off
REM Start the JScience Distributed Server
echo Starting JScience Server...

REM Check if Maven build is needed (simplified check)
if not exist "jscience-server\target\jscience-server-1.0.0-SNAPSHOT.jar" (
    echo Building Server...
    call mvn clean package -pl jscience-server -am -DskipTests
)

REM Run Server
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp jscience-server\target\jscience-server-1.0.0-SNAPSHOT.jar;jscience-server\target\lib\* org.jscience.server.JscienceServer
