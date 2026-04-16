@echo off
REM run_universal_performance_audit.bat
REM Runs NORMAL and EXACT Linear Algebra Performance Audits.

echo ==========================================
echo Starting Universal Performance Audits
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/2] Running NORMAL Performance Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=normal

echo [2/2] Running EXACT Performance Audit (CAUTION: High Precision)...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=exact

echo ==========================================
echo Performance Audits Completed.
echo JSON and PDF reports are in docs/benchmark-results/
echo ==========================================
pause
