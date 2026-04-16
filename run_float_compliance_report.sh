#!/bin/bash
# Episteme - Float Linear Algebra Compliance Report
# This script runs the LinearAlgebraComplianceTest focusing on Float/Single precision operations.

echo "[INFO] Initializing Float Linear Algebra Compliance Report..."

# Setup library paths for native BLAS/LAPACK
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
export LD_LIBRARY_PATH="$SCRIPT_DIR/libs:$SCRIPT_DIR/episteme-native/libs:$LD_LIBRARY_PATH"

# Enable Project Panama features and native access
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo "[INFO] Executing compliance suite in FLOAT mode..."
mvn test -Dtest=LinearAlgebraComplianceTest \
    -Dorg.episteme.test.suite=float \
    -Dorg.episteme.include.provider="FFM,CPU (Dense),CPU (Sparse),CUDA,OpenCL,SIMD,CPU-BLAS" \
    -Dorg.episteme.report.path=docs/FLOAT_ALGEBRA_COMPLIANCE_REPORT.md \
    -Dorg.episteme.project.name="Episteme Float Algebra" \
    -Dsurefire.failIfNoSpecifiedTests=false \
    -pl episteme-benchmarks -am

echo ""
if [ -f "docs/FLOAT_ALGEBRA_COMPLIANCE_REPORT.md" ]; then
    echo "[SUCCESS] Verification complete. Report generated at docs/FLOAT_ALGEBRA_COMPLIANCE_REPORT.md"
else
    echo "[ERROR] Report generation failed. Check maven logs above."
fi
