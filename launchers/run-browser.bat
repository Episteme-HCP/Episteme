@echo off
setlocal
cd /d %~dp0..
set APP_CLASS=org.jscience.apps.shared.ScienceBrowser
set LIB_DIR=launchers\libs\libs
set MODULES_DIR=launchers\libs
set MODULE_PATH=%MODULES_DIR%\jscience-featured-apps-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-core-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-natural-1.0.0-SNAPSHOT.jar;%MODULES_DIR%\jscience-social-1.0.0-SNAPSHOT.jar

@echo off
echo Starting JScience Knowledge Browser...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%LIB_DIR%" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*

endlocal
