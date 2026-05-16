@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

echo Starting Episteme in GPU Mode...
echo Note: Requires CUDA drivers installed.
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp target/classes;target/test-classes -Dorg.episteme.compute.mode=GPU org.episteme.Episteme
pause

endlocal

