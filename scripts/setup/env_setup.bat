@echo off
rem Episteme Centralized Environment Setup (Windows)
rem Called by other scripts to standardize library paths and variables.

if defined SETUP_COMPLETE goto :eof

rem --- Java Version Setup (Project requires JDK 21+) ---
if not defined JAVA_HOME (
    set "JAVA_HOME=C:\Program Files\Java\jdk-25"
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [WARNING] No compatible JDK 21+ found in JAVA_HOME. Searching...
    if exist "C:\Program Files\Java\jdk-25" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot" (
        set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot"
    ) else if exist "C:\Program Files\Java\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    )
)

if exist "%JAVA_HOME%\bin\java.exe" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    echo [INFO] Using JDK at: %JAVA_HOME%
) else (
    echo [ERROR] No compatible JDK 21+ detected! Please install JDK 25.
)

rem Standard Native Library Root
if not defined NATIVE_ROOT set "NATIVE_ROOT=C:\Episteme-Native"

rem Determine project root (relative to scripts/setup dir)
set "PROJECT_ROOT=%~dp0..\.."
set "LIBS_DIR=%PROJECT_ROOT%\libs"

rem Add Episteme project libs to PATH
if exist "%LIBS_DIR%" (
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
