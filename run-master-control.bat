@echo off
setlocal

set MODULE_PATH=jscience-core\target\classes;jscience-featured-apps\target\classes

echo Launching JScience Master Control...
java --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.jscience.core -m org.jscience.core.ui.JScienceMasterControl --monitor %*

endlocal
