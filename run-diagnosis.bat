@echo off
setlocal

rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\JScience-Native"
if exist "%NATIVE_ROOT%\OpenBLAS\bin" set "PATH=%NATIVE_ROOT%\OpenBLAS\bin;%PATH%"
if exist "%NATIVE_ROOT%\HDF5\bin" set "PATH=%NATIVE_ROOT%\HDF5\bin;%PATH%"
if exist "%NATIVE_ROOT%\FFTW3" set "PATH=%NATIVE_ROOT%\FFTW3;%PATH%"
if exist "%NATIVE_ROOT%\MPJ\bin" set "PATH=%NATIVE_ROOT%\MPJ\bin;%PATH%"

set MODULE_PATH=jscience-benchmarks\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes;jscience-native\target\classes
set DEPENDENCY_DIR=jscience-benchmarks\target\dependency
set LIB_DIR=launchers\lib

if defined MPJ_HOME (
    set "MPJ_JAR=%MPJ_HOME%\lib\mpj.jar"
) else (
    if exist "C:\JScience-Native\MPJ\lib\mpj.jar" set "MPJ_JAR=C:\JScience-Native\MPJ\lib\mpj.jar"
)

echo Compiling Diagnosis...
javac -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%" DiagnoseNative.java

echo Running Diagnosis...
java --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%;." DiagnoseNative

pause
endlocal
