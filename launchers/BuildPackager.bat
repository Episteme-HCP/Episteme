@echo off
rem Wrapper to run the PowerShell BuildPackager script
powershell -ExecutionPolicy Bypass -File "%~dp0BuildPackager.ps1"
if %ERRORLEVEL% NEQ 0 (
    echo Build failed with error code %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)
echo.
echo BuildPackager completed successfully.
pause
