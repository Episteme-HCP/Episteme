# Combined test runner for High-Precision Linear Algebra
$env:MAVEN_OPTS = "--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

Write-Host "[INFO] Running Numerical Correctness test from root..."
mvn.cmd -e test "-Dtest=NumericalCorrectnessTest" -DskipTests=false -DfailIfNoTests=false -Dsurefire.failIfNoSpecifiedTests=false -Denforcer.skip=true --no-transfer-progress

if (Test-Path docs/HIGH_PRECISION_COMPLIANCE_REPORT.md) {
    Write-Host "[SUCCESS] Compliance report generated."
} else {
    Write-Host "[FAILURE] Compliance report missing."
}

if (Test-Path docs/NUMERICAL_CORRECTNESS_REPORT.md) {
    Write-Host "[SUCCESS] Correctness report generated."
} else {
    Write-Host "[FAILURE] Correctness report missing."
}
