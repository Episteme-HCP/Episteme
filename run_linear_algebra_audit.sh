#!/bin/bash
# run_linear_algebra_audit.sh
# Specialized Correctness Audit for Linear Algebra (FAST, NORMAL, EXACT)
# Generates UNIVERSAL_AUDIT_FAST.md, UNIVERSAL_AUDIT_NORMAL.md, UNIVERSAL_AUDIT_EXACT.md

echo "=========================================="
echo "Starting Linear Algebra Correctness Audit"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector \
  --enable-preview \
  --enable-native-access=ALL-UNNAMED \
  -Depisteme.backend.disable.cuda-dense=false \
  -Depisteme.backend.disable.cuda-sparse=false \
  -Depisteme.backend.disable.opencl-dense=false \
  -Depisteme.backend.disable.opencl-sparse=false \
  -Depisteme.backend.disable.grpc-math=true"

echo "[1/3] Auditing FAST Mode (Float 32-bit)..."
mvn test -pl episteme-benchmarks -am -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=fast -Dsurefire.failIfNoSpecifiedTests=false

echo "[2/3] Auditing NORMAL Mode (Double 64-bit)..."
mvn test -pl episteme-benchmarks -am -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=normal -Dsurefire.failIfNoSpecifiedTests=false

echo "[3/3] Auditing EXACT Mode (Arbitrary Precision)..."
mvn test -pl episteme-benchmarks -am -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=exact -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "Correctness Audit Completed."
echo "Reports generated in docs/:"
echo " - LINEAR_ALGEBRA_AUDIT_FAST.md"
echo " - LINEAR_ALGEBRA_AUDIT_NORMAL.md"
echo " - LINEAR_ALGEBRA_AUDIT_EXACT.md"
echo "=========================================="
