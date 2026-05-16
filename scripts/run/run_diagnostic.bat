@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

set CP=episteme-benchmarks\target\classes;episteme-native\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-benchmarks\target\lib\*

set "LIBS_DIR=%~dp0libs"
if exist "%LIBS_DIR%" (
    set "PATH=%LIBS_DIR%;%PATH%"
)

set "EPISTEME_PYTHON=C:\Users\silve\AppData\Local\Programs\Python\Python314\python.exe"


java --enable-preview --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%CP%" org.episteme.benchmarks.BackendDiagnostic

endlocal

