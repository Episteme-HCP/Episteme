@echo off
setlocal

set APP_CLASS=org.jscience.apps.biology.PandemicForecasterApp
set LIB_DIR=lib
set MODULE_PATH=..\jscience-killer-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes

echo Starting Pandemic Forecaster...
java --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS%

endlocal
