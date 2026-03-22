#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:/usr/bin:/bin:$PATH
cd ~/Episteme
# Build including benchmarks module and all its dependencies (including native)
mvn clean compile -pl episteme-benchmarks -am -DskipTests
# Run the benchmark
mvn exec:java -pl episteme-benchmarks -Dexec.mainClass='org.episteme.benchmarks.cli.BenchmarkCLI' -Dexec.args='--run-all --domain Physics --iterations 1' -Pjcuda-linux-x86_64
