#!/bin/bash
# run_full_performance_benchmark.sh
# Launches the Full Multimodal Performance Benchmark (Single JVM)

echo "=========================================="
echo "Starting Full Multimodal Performance Benchmark"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

mvn clean test -pl episteme-benchmarks -am -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo "=========================================="
echo "Benchmark Completed."
echo "Reports generated in docs/benchmark-results/"
echo "=========================================="
