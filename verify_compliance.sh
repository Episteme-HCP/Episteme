#!/bin/bash
# Initialize SDKMAN
export SDKMAN_DIR="/home/silve/.sdkman"
if [ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]; then
    source "$SDKMAN_DIR/bin/sdkman-init.sh"
fi

cd /home/spinradnorman/Episteme

echo "=== Pulling latest changes ==="
git fetch origin
git reset --hard origin/main

echo "=== Building project ==="
mvn clean install -DskipTests

echo "=== Running LinearAlgebraComplianceTest (No Fork) ==="
# Removing incubator modules for stability if not found
mvn test -pl episteme-native -Dtest=LinearAlgebraComplianceTest -DforkCount=0

echo "=== Locating Benchmarks ==="
BENCHMARK_CLASS=$(find episteme-benchmarks/src/main/java -name "*Systematic*Benchmark.java" | sed 's/.*java\///;s/\.java//;s/\//./g' | head -n 1)

if [ ! -z "$BENCHMARK_CLASS" ]; then
    echo "=== Running Benchmark: $BENCHMARK_CLASS ==="
    mvn exec:java -pl episteme-benchmarks -Dexec.mainClass="$BENCHMARK_CLASS" -Dexec.cleanupDaemonThreads=false
else
    echo "WARNING: Could not find benchmark main class."
fi

echo "=== Verification Finished ==="
find . -name "*ComplianceReport.md"
find . -name "*BenchmarkReport.md"
