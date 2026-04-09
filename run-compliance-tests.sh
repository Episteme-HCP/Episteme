#!/bin/bash
echo "=========================================="
echo "Running High-Precision Compliance Report"
echo "=========================================="

# Check for pre-built JAR
SERVER_JAR="dist/episteme-server.jar"
if [ -f "$SERVER_JAR" ]; then
    echo "[INFO] Starting Episteme gRPC Server from JAR..."
    # Launch in background
    java --add-modules jdk.incubator.vector --enable-preview -Xmx2G -jar "$SERVER_JAR" > /dev/null 2>&1 &
    SERVER_PID=$!
else
    echo "[INFO] Starting Episteme gRPC Server using Maven (slower)..."
    mvn spring-boot:run -pl episteme-server -Dspring-boot.run.arguments="--grpc.server.port=50051" > /dev/null 2>&1 &
    SERVER_PID=$!
fi

echo "[INFO] Waiting for server to initialize (20s)..."
sleep 20

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo "[INFO] Executing HighPrecisionComplianceTest..."
mvn test -pl episteme-benchmarks -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path=docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo "[INFO] Executing LinearAlgebraComplianceTest (Standard)..."
mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.report.path=docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo "[INFO] Shutting down Episteme Server (PID: $SERVER_PID)..."
kill $SERVER_PID

echo ""
echo "Compliance reports generated at:"
echo "- docs/HIGH_PRECISION_COMPLIANCE_REPORT.md"
echo "- docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md"
