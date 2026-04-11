@echo off
setlocal
echo ==========================================
echo Running High-Precision Compliance Report
echo ==========================================

REM The Episteme Server is now managed internally by the test suite
REM To run in standalone mode (no gRPC), add -Depisteme.backend.disable.grpc-math=true -Depisteme.test.skip-server-startup=true to MAVEN_OPTS

echo [INFO] Starting High-Precision Compliance Audit...

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing HighPrecisionComplianceTest...
call mvn test -pl episteme-benchmarks -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path=docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo [INFO] Shutting down Episteme Server...
taskkill /FI "WINDOWTITLE eq EpistemeServer*" /T /F >nul 2>&1

echo.
echo Compliance report generated at:
echo - docs/HIGH_PRECISION_COMPLIANCE_REPORT.md
pause
endlocal

