@echo off
call "%~dp0scripts\setup\env_setup.bat"
setlocal

set APP_CLASS=org.episteme.core.ui.EpistemeDemosApp
set LIB_DIR=launchers\lib
set MODULE_PATH=episteme-featured-apps\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-social\target\classes;episteme-native\target\classes;episteme-client\target\classes;episteme-server\target\classes
echo Starting Episteme Demos Suite...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*

endlocal

