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

rem --- Java Version Setup (Project requires JDK 25+) ---
set "JDK25_PATH=C:\Program Files\Java\jdk-25"
if exist "%JDK25_PATH%\bin\java.exe" (
    echo [INFO] JDK 25 detected at %JDK25_PATH%.
    set "JAVA_HOME=%JDK25_PATH%"
    set "PATH=%JDK25_PATH%\bin;%PATH%"
) else (
    echo [WARNING] JDK 25 not found at %JDK25_PATH%. Compilation might fail if the default JDK is older.
)

echo Launching Episteme Master Control via Maven...
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
call mvn compile exec:java -pl episteme-featured-apps -am -Dexec.mainClass="org.episteme.core.ui.EpistemeMasterControl" -Dexec.args="%*"

endlocal
