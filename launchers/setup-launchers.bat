@echo off
echo Setting up launcher dependencies...
echo This script will download required JARs to the 'lib' directory.

cd ..\jscience-featured-apps
call mvn dependency:copy-dependencies "-DoutputDirectory=../launchers/lib" "-DincludeScope=runtime"
cd ..\launchers

echo.
echo Setup complete. You can now run the batch files.
pause
