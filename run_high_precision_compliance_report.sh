#!/bin/bash
echo "=========================================="
echo "Running High-Precision Compliance Report"
echo "=========================================="

# Start gRPC server in background
echo "[INFO] Starting Episteme gRPC Server (episteme-server)..."
mvn spring-boot:run -pl episteme-server -Dspring-boot.run.arguments="--grpc.server.port=50051" > server_startup.log 2>&1 &
SERVER_PID=$!

# Trap exit to kill server
trap "echo '[INFO] Shutting down Episteme Server...'; kill $SERVER_PID" EXIT

echo "[INFO] Waiting for server to initialize (15s)..."
sleep 15

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo "[INFO] Executing HighPrecisionComplianceTest..."
mvn clean test -pl episteme-benchmarks -am -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path=docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo ""
echo "Compliance report generated at docs/HIGH_PRECISION_COMPLIANCE_REPORT.md"
