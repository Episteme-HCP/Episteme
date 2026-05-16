@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

:: Determine the path to the episteme-core classes
set MODULE_PATH=episteme-core\target\classes;episteme-featured-apps\target\classes;episteme-benchmarks\target\classes
echo Launching Episteme Studio...
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path %MODULE_PATH% --add-modules javafx.controls,javafx.fxml,org.episteme.core -m org.episteme.core.ui.EpistemeDemosApp --monitor %*

endlocal

