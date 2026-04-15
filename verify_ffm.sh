#!/bin/bash
# Episteme - FFM Backend Isolated Verification
# This script runs the LinearAlgebraComplianceTest focusing only on the NativeFFM backend.

echo "[INFO] Initializing FFM Backend Verification..."

# Setup library paths for native BLAS/LAPACK
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
export LD_LIBRARY_PATH="$SCRIPT_DIR/libs:$SCRIPT_DIR/episteme-native/libs:$LD_LIBRARY_PATH"

# Enable Project Panama features and native access
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED"

echo "[INFO] Executing compliance suite for NativeFFM..."
mvn test -Dtest=LinearAlgebraComplianceTest \
    -Dorg.episteme.include.provider=NativeFFM \
    -Dorg.episteme.report.path=docs/FFM_VERIFICATION_REPORT.md \
    -Dorg.episteme.project.name="Episteme FFM" \
    -Dsurefire.failIfNoSpecifiedTests=false \
    -pl episteme-benchmarks -am

echo ""
if [ -f "docs/FFM_VERIFICATION_REPORT.md" ]; then
    echo "[SUCCESS] Verification complete. Report generated at docs/FFM_VERIFICATION_REPORT.md"
else
    echo "[ERROR] Report generation failed. Check maven logs above."
fi
