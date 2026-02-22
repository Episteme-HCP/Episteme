@echo off
setlocal

set APP_CLASS=org.jscience.apps.biology.PandemicForecasterApp
set LIB_DIR=lib
set MODULE_PATH=..\jscience-featured-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes

echo Starting Pandemic Forecaster...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" --monitor --monitor --monitor %APP_CLASS%

endlocal
