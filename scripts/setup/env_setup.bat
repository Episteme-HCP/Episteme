@echo off
rem Episteme Centralized Environment Setup (Windows)
rem Called by other scripts to standardize library paths and variables.

if defined SETUP_COMPLETE goto :eof

echo [INFO] Initializing Episteme Environment...

rem Determine project root
set "PROJECT_ROOT=%~dp0..\..\"
set "LIBS_DIR=%PROJECT_ROOT%libs"

rem --- Java Version Setup (JDK 25 preferred) ---
set "VALID_JAVA=0"

if defined JAVA_HOME (
    set "JAVA_HOME=%JAVA_HOME:"=%"
    if exist "%JAVA_HOME%\bin\java.exe" (
        "%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr "21 25" >nul && set "VALID_JAVA=1"
    )
)

if "%VALID_JAVA%"=="0" (
    echo [WARNING] No compatible JDK 21+ found in JAVA_HOME. Searching...
    if exist "C:\Program Files\Java\jdk-25" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25"
        set "VALID_JAVA=1"
        echo [INFO] Auto-detected JDK 25 at: C:\Program Files\Java\jdk-25
    )
)

if "%VALID_JAVA%"=="0" (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-25*" (
        for /d %%d in ("C:\Program Files\Eclipse Adoptium\jdk-25*") do (
            set "JAVA_HOME=%%d"
            set "VALID_JAVA=1"
            echo [INFO] Auto-detected JDK 25 at: %%d
        )
    )
)

if "%VALID_JAVA%"=="0" (
    echo [ERROR] No compatible JDK 21 or 25 or higher was found.
    echo [ERROR] Current JAVA_HOME: %JAVA_HOME%
    echo [ERROR] Please install JDK 25 or set JAVA_HOME to a valid JDK 21 plus installation.
    pause
    exit /b 1
)

rem Add JAVA_HOME/bin to PATH if not already there
set "PATH=%JAVA_HOME%\bin;%PATH%"

rem --- Maven Options (JVM Args for FFM and Vector API) ---
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

rem --- Native Libraries Setup ---

rem Standard Native Library Root (if exists)
if not defined NATIVE_ROOT set "NATIVE_ROOT=C:\Episteme-Native"

rem Add Episteme project libs to PATH
if exist "%LIBS_DIR%" (
    echo [INFO] Adding project libs to PATH: %LIBS_DIR%
    set "PATH=%LIBS_DIR%;%PATH%"
)

rem CUDA Setup
if not defined CUDA_PATH set "CUDA_PATH=C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v13.1"
if exist "%CUDA_PATH%\bin" (
    set "PATH=%CUDA_PATH%\bin;%CUDA_PATH%\libnvvp;%PATH%"
)

rem VLC Integration
if exist "C:\Program Files\VideoLAN\VLC" (
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)

rem Python Integration
if not defined EPISTEME_PYTHON (
    set "EPISTEME_PYTHON=C:\Users\silve\AppData\Local\Programs\Python\Python314\python.exe"
)

rem OpenBLAS / HDF5 / FFTW Fallbacks
if exist "%NATIVE_ROOT%\OpenBLAS\bin" set "PATH=%NATIVE_ROOT%\OpenBLAS\bin;%PATH%"
if exist "%NATIVE_ROOT%\HDF5\bin" set "PATH=%NATIVE_ROOT%\HDF5\bin;%PATH%"
if exist "%NATIVE_ROOT%\FFTW3" set "PATH=%NATIVE_ROOT%\FFTW3;%PATH%"

set SETUP_COMPLETE=true
echo [INFO] Episteme Environment Setup Complete.
echo.
