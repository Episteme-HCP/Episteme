@echo off
call "%~dp0scripts\setup\env_setup.bat"
setlocal

:menu
cls
echo ============================================
echo   Episteme Multi-Launcher
echo ============================================
echo.
echo Please select an application to launch:
echo.
echo  1) Web Browser
echo  2) Chemistry Simulation
echo  3) Civilization Model
echo  4) Crystal Lattice Viewer
echo  5) Episteme Demos Suite
echo  6) Grid Computing Dashboard
echo  7) Life Science Simulation
echo  8) Market Economy Model
echo  9) Pandemic Simulator
echo 10) Quantum Computing Workbench
echo 11) Spin Valve Simulation
echo 12) Stability Analyzer
echo 13) Titration Simulation
echo 14) Verification Suite
echo.
echo  Q) Quit
echo.
set /p choice="Enter choice (1-14 or Q): "

if /i "%choice%"=="Q" exit /b 0

set "LAUNCHER_DIR=%~dp0episteme-featured-apps\launchers"
set "SCRIPTS_DIR=%~dp0scripts"

if "%choice%"=="1"  call "%LAUNCHER_DIR%\run_browser.bat"
if "%choice%"=="2"  call "%LAUNCHER_DIR%\run_chemistry.bat"
if "%choice%"=="3"  call "%LAUNCHER_DIR%\run_civilization.bat"
if "%choice%"=="4"  call "%LAUNCHER_DIR%\run_crystal.bat"
if "%choice%"=="5"  call "%LAUNCHER_DIR%\run_episteme_demos.bat"
if "%choice%"=="6"  call "%LAUNCHER_DIR%\run_grid.bat"
if "%choice%"=="7"  call "%LAUNCHER_DIR%\run_life_science.bat"
if "%choice%"=="8"  call "%LAUNCHER_DIR%\run_market.bat"
if "%choice%"=="9"  call "%LAUNCHER_DIR%\run_pandemic.bat"
if "%choice%"=="10" call "%LAUNCHER_DIR%\run_quantum.bat"
if "%choice%"=="11" call "%LAUNCHER_DIR%\run_spin_valve.bat"
if "%choice%"=="12" call "%LAUNCHER_DIR%\run_stability.bat"
if "%choice%"=="13" call "%LAUNCHER_DIR%\run_titration.bat"
if "%choice%"=="14" call "%LAUNCHER_DIR%\run_verify.bat"

echo.
echo Press any key to return to menu...
pause >nul
goto menu

endlocal

