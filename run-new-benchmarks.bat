@echo off
setlocal

rem --- Argument Parsing ---
set "FILTER="
for %%a in (%*) do (
    if "%%a"=="--filter" set FILTER=%%b
)

set APP_CLASS=org.jscience.benchmarks.benchmark.BenchmarkRunner
set JAR_PATH=jscience-benchmarks\target\jscience-benchmarks.jar
set LIB_DIR=launchers\lib
set DEPENDENCY_DIR=jscience-benchmarks\target\dependency
set MODULE_PATH=jscience-benchmarks\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes;jscience-native\target\classes

rem --- Add Native JARs to Classpath ---
if defined MPJ_HOME (
    set "MPJ_JAR=%MPJ_HOME%\lib\mpj.jar"
) else (
    if exist "C:\JScience-Native\MPJ\lib\mpj.jar" set "MPJ_JAR=C:\JScience-Native\MPJ\lib\mpj.jar"
)

echo ==========================================
echo Running NEW JScience Benchmarks ONLY
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

echo [INFO] Running specific benchmarks...
java -verbose:class --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%" %APP_CLASS% --cli --filter=Systematic

echo.
exit /b 0
endlocal
