@echo off
REM run_full_performance_benchmark.bat
REM Launches the Full Multimodal Performance Benchmark (Single JVM)

echo ==========================================
echo Starting Full Multimodal Performance Benchmark
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

call mvn clean test -pl episteme-benchmarks -am -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo Benchmark Completed.
echo Reports generated in docs/benchmark-results/
echo ==========================================

