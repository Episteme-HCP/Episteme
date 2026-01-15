@echo off
setlocal
cd /d "%~dp0"
echo ============================================
echo   JScience Javadoc Generator
echo ============================================
echo.
echo This script will regenerate the Javadoc for the entire JScience project.
echo.

REM Optional: Clean old javadoc
if exist javadoc (
    echo Cleaning old javadoc folder...
    rmdir /s /q javadoc
)

echo.
echo Running Maven Javadoc Aggregate...
echo (This may take a few minutes)
echo.

REM We run compile first to ensure any generated sources (like Protobuf) are available
call mvn compile javadoc:aggregate -DreportOutputDirectory="%CD%" -DdestDir=javadoc -Dnotimestamp=true

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Javadoc generation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ============================================
echo   SUCCESS!
echo   Javadoc is available in: %CD%\javadoc
echo ============================================
pause
endlocal
