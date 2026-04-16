@echo off
REM run_universal_audit.bat
REM Runs FAST, NORMAL, and EXACT Linear Algebra Compliance Reports.

echo ==========================================
echo Starting Universal Linear Algebra Audits
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/3] Running FAST Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=fast

echo [2/3] Running NORMAL Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=normal

echo [3/3] Running EXACT Audit...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=exact

echo ==========================================
echo Audits Completed.
echo Reports generated in docs/:
echo  - UNIVERSAL_AUDIT_FAST.md
echo  - UNIVERSAL_AUDIT_NORMAL.md
echo  - UNIVERSAL_AUDIT_EXACT.md
echo ==========================================
pause
