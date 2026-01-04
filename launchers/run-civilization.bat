@echo off
setlocal

set APP_CLASS=org.jscience.apps.sociology.CivilizationApp
set LIB_DIR=lib
set MODULE_PATH=..\jscience-featured-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes;..\jscience-mathematics\target\classes

echo Starting Civilization Collapse Model...
java --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS%

endlocal
