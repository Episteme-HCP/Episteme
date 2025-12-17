#!/bin/bash
echo "Running JScience Benchmarks with Comparisons..."
mvn exec:java -Dexec.mainClass="org.jscience.benchmark.BenchmarkRunner" -Dexec.classpathScope=test
