@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

cd /d %~dp0..
set APP_CLASS=org.episteme.core.ui.EpistemeDemosApp
set LIB_DIR=..\..\launchers\lib
echo Starting Episteme Demos Suite...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%~dp0..\..\launchers\lib\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%~dp0..\target\classes;%~dp0..\..\episteme-featured-apps\target\classes;%~dp0..\..\episteme-core\target\classes;%~dp0..\..\episteme-natural\target\classes;%~dp0..\..\episteme-social\target\classes;%LIB_DIR%\*" %APP_CLASS% %*

endlocal

