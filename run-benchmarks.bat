@echo off
setlocal

rem --- Argument Parsing ---
set APP_CLASS=org.episteme.benchmarks.ui.Launcher
for %%a in (%*) do (
    if "%%a"=="--cli" set APP_CLASS=org.episteme.benchmarks.cli.BenchmarkCLI
    if "%%a"=="--shaded" set USE_SHADED_JAR=true
    if "%%a"=="-jar" set USE_SHADED_JAR=true
)
set JAR_PATH=episteme-benchmarks\target\episteme-benchmarks-1.0.0-SNAPSHOT.jar
set LIB_DIR=launchers\lib
set DEPENDENCY_DIR=episteme-benchmarks\target\lib
set MODULE_PATH=episteme-benchmarks\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-social\target\classes;episteme-native\target\classes;episteme-client\target\classes;episteme-server\target\classes

rem --- Add Native JARs to Classpath ---
if defined MPJ_HOME (
    set "MPJ_JAR=%MPJ_HOME%\lib\mpj.jar"
) else (
    rem Fallback if MPJ_HOME not set but folder exists
    if exist "C:\Episteme-Native\MPJ\lib\mpj.jar" set "MPJ_JAR=C:\Episteme-Native\MPJ\lib\mpj.jar"
)


echo ==========================================
echo Running Episteme Benchmarks
echo ==========================================

rem --- Native Libraries Setup ---
set "NATIVE_ROOT=C:\Episteme-Native"
set "LIBS_DIR=%~dp0libs"

rem --- Python (Qiskit) Integration ---
if not defined EPISTEME_PYTHON (
    set "EPISTEME_PYTHON=C:\Users\silve\AppData\Local\Programs\Python\Python314\python.exe"
)

rem --- VLC ---
if exist "C:\Program Files\VideoLAN\VLC" (
    echo [INFO] Adding VLC to PATH...
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)

rem --- Project libs directory (ODE, QuEST, oneDNN, lz4, etc.) ---
if exist "%LIBS_DIR%" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%LIBS_DIR%;%PATH%"
)

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
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -Djava.library.path="libs/native" -cp "%JAR_PATH%;%DEPENDENCY_DIR%\*" %APP_CLASS% %*
) else (
    echo [INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR.
    if not exist "episteme-benchmarks\target\classes" (
        echo [INFO] Classes not found, building module...
        call mvn compile -pl episteme-benchmarks -am -DskipTests
    )
    if not exist "%DEPENDENCY_DIR%" (
        echo [INFO] Dependencies not found in target, copying...
        call mvn dependency:copy-dependencies -pl episteme-benchmarks -DincludeScope=runtime -DskipTests
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%" %APP_CLASS% %*

)

echo.
pause
endlocal
