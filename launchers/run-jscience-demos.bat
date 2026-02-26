@echo off
setlocal
cd /d %~dp0..
set APP_CLASS=org.jscience.core.ui.JScienceDemosApp
set LIB_DIR=launchers\libs\libs
set MODULES_DIR=launchers\libs
set MODULE_PATH=%MODULES_DIR%\jscience-featured-apps-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-core-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-natural-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-social-1.0.0-SNAPSHOT.jar

set PATH=%~dp0..\libs;%PATH%
set PATH=%~dp0..\libs;%PATH%
echo Starting JScience Demo Suite...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%~dp0libs\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%~dp0..\jscience-featured-apps\target\classes;%~dp0..\jscience-core\target\classes;%~dp0..\jscience-natural\target\classes;%~dp0..\jscience-social\target\classes;%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*

endlocal
