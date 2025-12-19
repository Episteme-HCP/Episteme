@echo off
rem Script to run JScience Benchmarks on Windows

echo Building Benchmarks Module...
call mvn clean package -pl jscience-benchmarks -am -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)

echo.
echo ==========================================
echo Running JScience Benchmarks
echo ==========================================
echo.

java -cp jscience-benchmarks/target/jscience-benchmarks.jar org.jscience.benchmark.BenchmarkRunner
pause
