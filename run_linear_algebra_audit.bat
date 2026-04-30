@echo off
REM run_linear_algebra_audit.bat
REM Specialized Correctness Audit for Linear Algebra (FAST, NORMAL, EXACT)
REM Generates UNIVERSAL_AUDIT_FAST.md, UNIVERSAL_AUDIT_NORMAL.md, UNIVERSAL_AUDIT_EXACT.md

echo ==========================================
echo Starting Linear Algebra Correctness Audit
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/3] Auditing FAST Mode (Float 32-bit)...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=fast -Dsurefire.failIfNoSpecifiedTests=false

echo [2/3] Auditing NORMAL Mode (Double 64-bit)...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=normal -Dsurefire.failIfNoSpecifiedTests=false

echo [3/3] Auditing EXACT Mode (Arbitrary Precision)...
call mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.precision=exact -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo Correctness Audit Completed.
echo Reports generated in docs/:
echo  - LINEAR_ALGEBRA_AUDIT_FAST.md
echo  - LINEAR_ALGEBRA_AUDIT_NORMAL.md
echo  - LINEAR_ALGEBRA_AUDIT_EXACT.md
echo ==========================================
