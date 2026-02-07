@echo off
setlocal

:: Determine the path to the jscience-core classes
set MODULE_PATH=jscience-core\target\classes;jscience-featured-apps\target\classes;jscience-benchmarks\target\classes

echo Launching JScience Studio...
java --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.jscience.core -m org.jscience.core.ui.JScienceDemosApp --monitor %*

endlocal
