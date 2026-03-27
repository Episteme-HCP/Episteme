@echo off
setlocal
echo ==========================================
echo Running High-Precision Compliance Report
echo ==========================================

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing HighPrecisionComplianceTest...
call mvn clean test -pl episteme-benchmarks -am -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path=../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo.
echo Compliance report generated at docs/HIGH_PRECISION_COMPLIANCE_REPORT.md
pause
endlocal
