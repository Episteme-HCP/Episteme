@echo off
REM run_full_performance_benchmark.bat
REM Launches the Full Multimodal Performance Benchmark (FAST, NORMAL, EXACT)
REM Generates JSON and PDF reports with mode comparison graphs.

echo ==========================================
echo Starting Full Multimodal Performance Benchmark
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo Running Universal Multimodal Audit...
call mvn clean test -pl episteme-benchmarks -am "-Drevision=1.0.0-beta2" -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo Benchmark Completed.
echo Reports generated in docs/benchmark-results/:
echo  - linear_algebra_performance_multimodal_YYYYMMDD-HHMMSS.json
echo  - linear_algebra_performance_multimodal_YYYYMMDD-HHMMSS.pdf
echo  - LINEAR_ALGEBRA_PERFORMANCE_MULTIMODAL_YYYYMMDD-HHMMSS.md
echo ==========================================
