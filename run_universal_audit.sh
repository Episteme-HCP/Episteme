#!/bin/bash
# run_universal_audit.sh
# Launches the Universal Multimodal Performance Audit (Single JVM)

echo "=========================================="
echo "Starting Universal Multimodal Performance Audit"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

mvn test -pl episteme-benchmarks -am -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "Audit Completed."
echo "Reports generated in docs/benchmark-results/"
echo "=========================================="
