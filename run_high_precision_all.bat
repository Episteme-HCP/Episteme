@echo off
setlocal
echo ==========================================
echo Compiling the project once...
echo ==========================================

call mvn install -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo ==========================================
echo 1. Running High-Precision Compliance Test
echo ==========================================
call mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path="%~dp0docs/HIGH_PRECISION_COMPLIANCE_REPORT.md" -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo 2. Running High-Precision Correctness Test
echo ==========================================
call mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionCorrectnessTest -Dorg.episteme.report.path="%~dp0docs/HIGH_PRECISION_CORRECTNESS_REPORT.md" -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo 3. Running High-Precision Performance Test
echo ==========================================
call mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionPerformanceTest -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo All High-Precision tests completed.
pause
endlocal
