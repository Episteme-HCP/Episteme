#!/bin/bash
# run_linear_algebra_audit.sh
# Specialized Audit for Linear Algebra (Performance + Correctness)

echo "=========================================="
echo "Starting Linear Algebra Audit"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

echo "[1/3] Running Linear Algebra Performance Audit (Normal Precision)..."
mvn clean test -pl episteme-benchmarks -am -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=normal -Dsurefire.failIfNoSpecifiedTests=false

echo "[2/3] Running Linear Algebra Double Compliance Test..."
mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dsurefire.failIfNoSpecifiedTests=false

echo "[3/3] Running Linear Algebra Float Compliance Test..."
mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraFloatComplianceTest -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "Linear Algebra Audit Completed."
echo "Reports generated in docs/benchmark-results/ and target/surefire-reports/"
echo "=========================================="
