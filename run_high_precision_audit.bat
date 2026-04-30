@echo off
REM run_high_precision_audit.bat
REM Specialized Audit for High-Precision Math (Performance + Correctness)

echo ==========================================
echo Starting High-Precision Math Audit
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/2] Running High-Precision Performance Audit (EXACT Mode)...
call mvn clean test -pl episteme-benchmarks -am -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=exact -Dsurefire.failIfNoSpecifiedTests=false

echo [2/2] Running MPFR Precision Compliance Test...
call mvn test -pl episteme-native -Dtest=MPFRPrecisionComplianceTest -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo High-Precision Math Audit Completed.
echo Reports generated in docs/benchmark-results/ and target/surefire-reports/
echo ==========================================
