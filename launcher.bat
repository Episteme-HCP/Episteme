@echo off
cls
:menu
echo ==========================================
echo      JScience Demo Launcher
echo ==========================================
echo.
echo 1. Run 3D Solar System Viewer
echo 2. Run Matrix Benchmarks
echo 3. Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" goto solar
if "%choice%"=="2" goto benchmark
if "%choice%"=="3" goto exit
echo Invalid choice. Please try again.
pause
goto menu

:solar
echo.
echo Launching 3D Solar System Viewer...
echo ------------------------------------------
call mvn exec:java -Dexec.mainClass="org.jscience.physics.astronomy.view.SolarSystemDemo"
echo.
pause
goto menu

:benchmark
echo.
echo Launching Matrix Benchmarks...
echo ------------------------------------------
if exist run-benchmarks.bat (
    call run-benchmarks.bat
) else (
    echo run-benchmarks.bat not found. Running via Maven...
    call mvn exec:java -Dexec.mainClass="org.jscience.benchmark.BenchmarkRunner"
)
echo.
pause
goto menu

:exit
exit
