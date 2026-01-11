@echo off
setlocal enabledelayedexpansion

set APP_CLASS=org.jscience.ui.JScienceMasterControl
set LIB_DIR=launchers\lib
set MODULE_PATH=jscience-featured-apps\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes


set CLASSPATH=%MODULE_PATH%;%LIB_DIR%\*

echo Starting JScience Master Control...
java --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "!CLASSPATH!" %APP_CLASS%

pause
endlocal
