@echo off
call "%~dp0scripts\setup\env_setup.bat"
setlocal

echo Launching Episteme Master Control (Pre-compiled mode)...
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

call mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.core.ui.EpistemeMasterControl" -Dexec.args="%*"

endlocal

