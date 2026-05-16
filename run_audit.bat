@echo off
REM Use environment JAVA_HOME or system default java
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -Dorg.slf4j.simpleLogger.defaultLogLevel=info

echo --- Starting Episteme Linear Algebra Audit (v1.0.0-beta3) ---
if not "%JAVA_HOME%"=="" echo Using JAVA_HOME: %JAVA_HOME%

echo [1/3] Running Normal Precision Audit...
call mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=normal -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_NORMAL.md

echo [2/3] Running Fast Precision Audit...
call mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=fast -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_FAST.md

echo [3/3] Running Exact Precision Audit...
call mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=exact -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_EXACT.md

echo --- Audit Complete ---
