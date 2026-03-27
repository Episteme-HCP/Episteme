@echo off
setlocal
echo ==========================================
echo Running High-Precision Performance Report
echo ==========================================

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing HighPrecisionPerformanceTest...
call mvn clean test -pl episteme-benchmarks -am -Dtest=HighPrecisionPerformanceTest -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo Performance report generated in docs/benchmark-results/
pause
endlocal
