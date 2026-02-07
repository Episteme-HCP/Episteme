@echo off
setlocal

set APP_CLASS=org.jscience.benchmarks.benchmark.BenchmarkRunner
set JAR_PATH=jscience-benchmarks\target\jscience-benchmarks.jar
set LIB_DIR=launchers\lib
set MODULE_PATH=jscience-benchmarks\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes

echo ==========================================
echo Running JScience Benchmarks
echo ==========================================

rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\JScience-Native"

if exist "%NATIVE_ROOT%\OpenBLAS\bin" (
    echo [INFO] Adding OpenBLAS to PATH...
    set "PATH=%NATIVE_ROOT%\OpenBLAS\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\HDF5\bin" (
    echo [INFO] Adding HDF5 to PATH...
    set "PATH=%NATIVE_ROOT%\HDF5\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\FFTW3" (
    echo [INFO] Adding FFTW3 to PATH...
    set "PATH=%NATIVE_ROOT%\FFTW3;%PATH%"
)

if exist "%JAR_PATH%" (
    echo [INFO] Found shaded JAR, launching...
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar "%JAR_PATH%" %*
) else (
    echo [INFO] Shaded JAR not found, falling back to classes...
    if not exist "jscience-benchmarks\target\classes" (
        echo [INFO] Classes not found, building module...
        call mvn compile -pl jscience-benchmarks -am -DskipTests
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%LIB_DIR%\*" %APP_CLASS% %*
)

echo.
pause
endlocal
