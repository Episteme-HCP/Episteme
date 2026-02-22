@echo off
setlocal

set APP_CLASS=org.jscience.apps.physics.spintronics.SpinValveApp
set LIB_DIR=lib
set CLASSPATH_DIRS=..\jscience-featured-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes;..\jscience-benchmarks\target\classes

echo Starting JScience Spin Valve Simulator...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.swing -cp "%CLASSPATH_DIRS%;%LIB_DIR%\*" --monitor --monitor --monitor %APP_CLASS%

endlocal
