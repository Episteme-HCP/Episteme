@echo off
setlocal
echo ==========================================
echo Running High-Precision Compliance Report
echo ==========================================

REM Check for pre-built JAR
set SERVER_JAR=dist\episteme-server.jar
if exist "%SERVER_JAR%" GOTO START_JAR

echo [INFO] Starting Episteme gRPC Server using Maven (slower)...
echo [TIP] Run build_server_release.bat to speed up this process next time.
start "EpistemeServer" /min mvn spring-boot:run -pl episteme-server -Dspring-boot.run.arguments="--grpc.server.port=50051"
GOTO AFTER_SERVER

:START_JAR
echo [INFO] Starting Episteme gRPC Server from JAR...
start "EpistemeServer" /min java --add-modules jdk.incubator.vector --enable-preview -Xmx2G -jar "%SERVER_JAR%"

:AFTER_SERVER


echo [INFO] Waiting for server to initialize (15s)...
timeout /t 15 /nobreak

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing HighPrecisionComplianceTest...
call mvn test -pl episteme-benchmarks -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path=docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo [INFO] Shutting down Episteme Server...
taskkill /FI "WINDOWTITLE eq EpistemeServer*" /T /F >nul 2>&1

echo.
echo Compliance report generated at:
echo - docs/HIGH_PRECISION_COMPLIANCE_REPORT.md

endlocal

