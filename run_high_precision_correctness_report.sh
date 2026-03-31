#!/bin/bash
echo "=========================================="
echo "Running Numerical Correctness Report"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo "[INFO] Executing HighPrecisionCorrectnessTest..."
mvn clean test -pl episteme-benchmarks -am -Dtest=HighPrecisionCorrectnessTest -Dorg.episteme.report.path=docs/HIGH_PRECISION_CORRECTNESS_REPORT.md -DskipTests=false -Dsurefire.failIfNoSpecifiedTests=false

echo ""
echo "Correctness report generated at docs/HIGH_PRECISION_CORRECTNESS_REPORT.md"
