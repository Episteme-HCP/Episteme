#!/bin/bash
# Script to run JScience Benchmarks on Linux/Mac

echo "Building Benchmarks Module..."
mvn clean package -pl jscience-benchmarks -am -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo ""
echo "=========================================="
echo "Running JScience Benchmarks"
echo "=========================================="
echo ""

java -cp jscience-benchmarks/target/jscience-benchmarks.jar org.jscience.benchmark.BenchmarkRunner
