@echo off
setlocal
set APP_CLASS=org.jscience.apps.sociology.GlobalStabilityStudio
set LIB_DIR=launchers\lib
set MODULE_PATH=jscience-featured-apps\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes

echo Starting Global Stability Studio...
java --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" --monitor --monitor --monitor %APP_CLASS%

endlocal
