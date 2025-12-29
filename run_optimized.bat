@echo off
echo Starting JScience with Optimized JVM Settings...
set ERROR_CODE=0

:: Check for Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java not found in PATH.
    pause
    exit /b 1
)

:: Run with optimizations
:: Adjust memory (-Xmx) as needed for your system
java -server ^
     -XX:+UseG1GC ^
     -XX:MaxGCPauseMillis=200 ^
     -XX:+ParallelRefProcEnabled ^
     -XX:+UseStringDeduplication ^
     -XX:+AlwaysPreTouch ^
     -Xms2G -Xmx4G ^
     -cp "jscience-natural/target/classes;jscience-core/target/classes;jscience-social/target/classes;lib/*;target/dependency/*" ^
     org.jscience.JScience

if %ERRORLEVEL% NEQ 0 (
    echo Application exited with error code %ERRORLEVEL%
    pause
)
