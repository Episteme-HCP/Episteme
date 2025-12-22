@echo off
setlocal

set APP_CLASS=org.jscience.apps.biology.PandemicForecasterApp
set MODULE_PATH=..\jscience-killer-apps\target\classes;..\jscience-core\target\classes;..\jscience-natural\target\classes;..\jscience-social\target\classes;..\jscience-mathematics\target\classes
set LIBS=C:\Users\silve\.m2\repository\org\openjfx\javafx-controls\17.0.2\javafx-controls-17.0.2-win.jar;C:\Users\silve\.m2\repository\org\openjfx\javafx-graphics\17.0.2\javafx-graphics-17.0.2-win.jar;C:\Users\silve\.m2\repository\org\openjfx\javafx-base\17.0.2\javafx-base-17.0.2-win.jar;C:\Users\silve\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.15.2\jackson-databind-2.15.2.jar;C:\Users\silve\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.15.2\jackson-core-2.15.2.jar;C:\Users\silve\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.15.2\jackson-annotations-2.15.2.jar

echo Starting Pandemic Forecaster...
java --module-path "%LIBS%" --add-modules javafx.controls,javafx.fxml -cp "%MODULE_PATH%;%LIBS%" %APP_CLASS%

endlocal
