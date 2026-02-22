@echo off
setlocal

:: Determine the path to the jscience-core classes
set MODULE_PATH=jscience-core\target\classes;jscience-featured-apps\target\classes;jscience-benchmarks\target\classes

echo Launching JScience Studio...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.jscience.core -m org.jscience.core.ui.JScienceDemosApp --monitor %*

endlocal
