#!/bin/bash
# run_universal_performance_audit.sh
# Runs NORMAL and EXACT Linear Algebra Performance Audits.

echo "=========================================="
echo "Starting Universal Performance Audits"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

echo "Redirecting to run_universal_audit.sh..."
./run_universal_audit.sh

echo "=========================================="
echo "Performance Audits Completed."
echo "JSON and PDF reports are in docs/benchmark-results/"
echo "=========================================="
