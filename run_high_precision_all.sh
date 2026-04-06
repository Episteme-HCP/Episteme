#!/bin/bash
# Episteme High-Precision All-in-One Test Script
# Ce script compile et lance les tests de haute précision.

set -e

echo "=========================================="
echo "Compiling the project once..."
echo "=========================================="

# Compilation rapide
mvn install -DskipTests

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed."
    exit 1
fi

# Configuration des options Maven pour le Vector API et l'ouverture des packages
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

BASE_DIR="$(pwd)"

echo "=========================================="
echo "1. Running High-Precision Compliance Test"
echo "=========================================="
mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionComplianceTest -Dorg.episteme.report.path="$BASE_DIR/docs/HIGH_PRECISION_COMPLIANCE_REPORT.md" -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "2. Running High-Precision Correctness Test"
echo "=========================================="
mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionCorrectnessTest -Dorg.episteme.report.path="$BASE_DIR/docs/HIGH_PRECISION_CORRECTNESS_REPORT.md" -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "3. Running High-Precision Performance Test"
echo "=========================================="
mvn surefire:test -pl episteme-benchmarks -Dtest=HighPrecisionPerformanceTest -Dsurefire.failIfNoSpecifiedTests=false

echo ""
echo "All High-Precision tests completed."
