@echo off
setlocal

echo Running MPFR Transcendental Precision Tests...
if not exist "episteme-core\target\classes" mkdir "episteme-core\target\classes"

rem --- Native Libraries Setup ---
if exist "C:\Program Files\VideoLAN\VLC" (
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)
if exist "%~dp0libs" (
    set "PATH=%~dp0libs;%PATH%"
)

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED
mvn clean test -Dtest=MPFRTranscendentalTest -pl episteme-native -am -DfailIfNoSpecifiedTests=false -Dsurefire.useFile=false -Dorg.slf4j.simpleLogger.defaultLogLevel=info

echo.
echo Tests completed.
endlocal
