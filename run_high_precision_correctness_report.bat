@echo off
setlocal
echo ==========================================
echo Running Numerical Correctness Report
echo ==========================================

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Building and Executing HighPrecisionCorrectnessTest...
call mvn clean install -DskipTests
call mvn test -pl episteme-benchmarks -Dtest=HighPrecisionCorrectnessTest -Dorg.episteme.report.path="%~dp0docs/HIGH_PRECISION_CORRECTNESS_REPORT.md" -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo Correctness report generated at %~dp0docs/HIGH_PRECISION_CORRECTNESS_REPORT.md
endlocal
