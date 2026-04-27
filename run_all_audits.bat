@echo off
REM run_all_audits.bat
REM Runs FAST, NORMAL, and EXACT Linear Algebra Performance Audits.

echo ==========================================
echo Starting Universal Performance Audits (Full Suite)
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/3] Running FAST Performance Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=fast

echo [2/3] Running NORMAL Performance Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=normal

echo [3/3] Running EXACT Performance Audit (High Precision)...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraPerformanceAudit -Dorg.episteme.test.precision=exact

echo ==========================================
echo All Performance Audits Completed.
echo JSON and PDF reports are in docs/benchmark-results/
echo ==========================================
pause
