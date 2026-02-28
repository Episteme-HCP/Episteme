@echo off
setlocal
echo Starting Episteme in CPU Mode...


rem --- Native Libraries Setup ---
if exist "C:\Program Files\VideoLAN\VLC" (
    echo [INFO] Adding VLC to PATH...
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)
if exist "%~dp0libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0libs;%PATH%"
)
if exist "%~dp0..\libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0..\libs;%PATH%"
)

set "NATIVE_ROOT=C:\Episteme-Native"

if exist "%NATIVE_ROOT%\OpenBLAS\bin" (
    echo Adding OpenBLAS to PATH...
    set "PATH=%NATIVE_ROOT%\OpenBLAS\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\HDF5\bin" (
    echo Adding HDF5 to PATH...
    set "PATH=%NATIVE_ROOT%\HDF5\bin;%PATH%"
)
if exist "%NATIVE_ROOT%\FFTW3" (
    echo Adding FFTW3 to PATH...
    set "PATH=%NATIVE_ROOT%\FFTW3;%PATH%"
)
if exist "%NATIVE_ROOT%\MPJ\bin" (
    echo Adding MPJ Express to PATH...
    set "PATH=%NATIVE_ROOT%\MPJ\bin;%PATH%"
)

rem Check for Vector API module support (Java 21+)
rem We add --add-modules jdk.incubator.vector to enable SIMD if using modern Java
java --add-modules jdk.incubator.vector -cp target/classes;target/test-classes -Dorg.episteme.compute.mode=CPU org.episteme.Episteme
pause
endlocal
