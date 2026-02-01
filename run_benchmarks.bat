@echo off
setlocal
rem Script to run JScience Benchmarks on Windows

echo Building Benchmarks Module...
call mvn clean package -pl jscience-benchmarks -am -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)

echo.
echo ==========================================
echo Running JScience Benchmarks
echo ==========================================
echo.

rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\JScience-Native"

if exist "%NATIVE_ROOT%\OpenBLAS\bin" (
    echo Adding OpenBLAS to PATH...
    set "PATH=%NATIVE_ROOT%\OpenBLAS\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\HDF5\bin" (
    echo Adding HDF5 to PATH...
    set "PATH=%NATIVE_ROOT%\HDF5\bin;%PATH%"
)

rem Using --enable-native-access=ALL-UNNAMED for Panama if needed, and --add-modules jdk.incubator.vector for SIMD
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp jscience-benchmarks/target/jscience-benchmarks-1.0-SNAPSHOT.jar org.openjdk.jmh.Main
pause
endlocal
