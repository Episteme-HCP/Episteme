@echo off
cls
:menu
echo ==========================================
echo      JScience Demo Launcher
echo ==========================================
echo.
echo 1. Run 3D Solar System Viewer
echo 2. Run molecular Viewer (Water H2O)
echo 3. Run Matrix Viewer
echo 4. Run Plotting Demo (Sine Wave)
echo 5. Run Matrix Benchmarks
echo 6. Exit
echo.
set /p choice="Enter your choice (1-6): "

if "%choice%"=="1" goto solar
if "%choice%"=="2" goto chemistry
if "%choice%"=="3" goto matrix
if "%choice%"=="4" goto plot
if "%choice%"=="5" goto benchmark
if "%choice%"=="6" goto exit
echo Invalid choice. Please try again.
pause
goto menu

:solar
echo.
echo Launching 3D Solar System Viewer...
echo ------------------------------------------
call mvn exec:java -Dexec.mainClass="org.jscience.ui.astronomy.AstronomyViewer"
echo.
pause
goto menu

:chemistry
echo.
echo Launching Molecular Viewer...
echo ------------------------------------------
call mvn exec:java -Dexec.mainClass="org.jscience.ui.chemistry.MolecularViewerDemo"
echo.
pause
goto menu

:matrix
echo.
echo Launching Matrix Viewer...
echo ------------------------------------------
call mvn exec:java -Dexec.mainClass="org.jscience.ui.matrix.MatrixViewerDemo"
echo.
pause
goto menu

:plot
echo.
echo Launching Plotting Demo...
echo ------------------------------------------
call mvn exec:java -Dexec.mainClass="org.jscience.ui.plotting.PlottingDemo"
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
