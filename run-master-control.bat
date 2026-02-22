@echo off
setlocal

echo Launching JScience Master Control via Maven...
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
call mvn exec:java -pl jscience-featured-apps -Dexec.mainClass="org.jscience.core.ui.JScienceMasterControl" -Dexec.args="%*"

endlocal
