@echo off
REM Start the JScience Distributed Server
echo Starting JScience Server...

REM Check if Maven build is needed (simplified check)
if not exist "jscience-server\target\jscience-server-1.0.0-SNAPSHOT.jar" (
    echo Building Server...
    call mvn clean package -pl jscience-server -am -DskipTests
)

REM Run Server
java -cp jscience-server\target\jscience-server-1.0.0-SNAPSHOT.jar;jscience-server\target\lib\* org.jscience.server.JscienceServer
