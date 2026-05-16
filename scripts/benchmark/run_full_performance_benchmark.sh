#!/bin/bash
source "$(dirname "$0")/../../scripts/setup/env_setup.sh"

# run_full_performance_benchmark.sh
# Launches the Full Multimodal Performance Benchmark (FAST, NORMAL, EXACT)
# Generates JSON and PDF reports with mode comparison graphs.

echo "=========================================="
echo "Starting Full Multimodal Performance Benchmark"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

echo "Running Universal Multimodal Audit..."
mvn clean test -pl episteme-benchmarks -am -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "Benchmark Completed."
echo "Reports generated in docs/benchmark-results/:"
echo " - universal_multimodal_audit.json"
echo " - universal_multimodal_audit.pdf"
echo " - UNIVERSAL_MULTIMODAL_AUDIT.md"
echo "=========================================="

