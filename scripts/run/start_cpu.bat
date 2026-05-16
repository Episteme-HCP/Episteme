@echo off
call "%~dp0..\..\scripts\setup\env_setup.bat"
setlocal

echo Starting Episteme in CPU Mode...


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

