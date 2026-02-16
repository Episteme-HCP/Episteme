@echo off
setlocal

rem --- Argument Parsing ---
set APP_CLASS=org.jscience.benchmarks.ui.JScienceBenchmarkingApp
for %%a in (%*) do (
    if "%%a"=="--cli" set APP_CLASS=org.jscience.benchmarks.cli.BenchmarkCLI
    if "%%a"=="--shaded" set USE_SHADED_JAR=true
    if "%%a"=="-jar" set USE_SHADED_JAR=true
)
set JAR_PATH=jscience-benchmarks\target\jscience-benchmarks.jar
set LIB_DIR=launchers\lib
set DEPENDENCY_DIR=jscience-benchmarks\target\dependency
set MODULE_PATH=jscience-benchmarks\target\classes;jscience-core\target\classes;jscience-natural\target\classes;jscience-social\target\classes;jscience-native\target\classes;jscience-client\target\classes;jscience-server\target\classes

rem --- Add Native JARs to Classpath ---
if defined MPJ_HOME (
    set "MPJ_JAR=%MPJ_HOME%\lib\mpj.jar"
) else (
    rem Fallback if MPJ_HOME not set but folder exists
    if exist "C:\JScience-Native\MPJ\lib\mpj.jar" set "MPJ_JAR=C:\JScience-Native\MPJ\lib\mpj.jar"
)


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
) else if exist "C:\Program Files\HDF_Group\HDF5\2.0.0\bin" (
    echo [INFO] Adding HDF5 ^(official install^) to PATH...
    set "PATH=C:\Program Files\HDF_Group\HDF5\2.0.0\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\FFTW3" (
    echo [INFO] Adding FFTW3 to PATH...
    set "PATH=%NATIVE_ROOT%\FFTW3;%PATH%"
)

if defined USE_SHADED_JAR (
    echo [INFO] Flag detected: Forcing use of Shaded JAR...
    if not exist "%JAR_PATH%" (
        echo [ERROR] Shaded JAR not found at %JAR_PATH%
        echo [INFO] Please run 'mvn package -DskipTests' first.
        pause
        exit /b 1
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -Djava.library.path="libs/native" -jar "%JAR_PATH%" %*
) else (
    echo [INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR.
    if not exist "jscience-benchmarks\target\classes" (
        echo [INFO] Classes not found, building module...
        call mvn compile -pl jscience-benchmarks -am -DskipTests
    )
    if not exist "%DEPENDENCY_DIR%" (
        echo [INFO] Dependencies not found in target, copying...
        call mvn dependency:copy-dependencies -pl jscience-benchmarks -DincludeScope=runtime -DskipTests
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%" %APP_CLASS% %*

)

echo.
pause
endlocal
