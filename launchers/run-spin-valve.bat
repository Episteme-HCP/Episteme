@echo off
setlocal

set APP_CLASS=org.jscience.apps.physics.spintronics.SpinValveApp
set LIB_DIR=lib
set CLASSPATH_DIRS=..\jscience-featured-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes;..\jscience-benchmarks\target\classes

echo Starting JScience Spin Valve Simulator...
java --module-path "%LIB_DIR%\javafx" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.swing -cp "%CLASSPATH_DIRS%;%LIB_DIR%\*" %APP_CLASS%

endlocal
