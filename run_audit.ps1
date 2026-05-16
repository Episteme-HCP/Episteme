$env:MAVEN_OPTS = "--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -Dorg.slf4j.simpleLogger.defaultLogLevel=info"

Write-Host "--- Starting Episteme Linear Algebra Audit (v1.0.0-beta3) ---" -ForegroundColor Cyan

# 1. Normal Precision Audit
Write-Host "[1/3] Running Normal Precision Audit..." -ForegroundColor Green
cmd /c "mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=normal -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_NORMAL.md"

# 2. Fast Precision Audit
Write-Host "[2/3] Running Fast Precision Audit..." -ForegroundColor Green
cmd /c "mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=fast -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_FAST.md"

# 3. Exact Precision Audit (High Precision)
Write-Host "[3/3] Running Exact Precision Audit..." -ForegroundColor Green
cmd /c "mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -DskipTests=false -DfailIfNoTests=false -Dorg.episteme.test.precision=exact -Dorg.episteme.report.path=LINEAR_ALGEBRA_AUDIT_EXACT.md"

Write-Host "--- Audit Complete. Reports generated. ---" -ForegroundColor Cyan
