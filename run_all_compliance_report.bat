@echo off
setlocal
echo [INFO] Initializing Full Linear Algebra Compliance Report (Windows)...

set "SCRIPT_DIR=%~dp0"
set "PATH=%SCRIPT_DIR%libs;%SCRIPT_DIR%episteme-native\libs;%PATH%"

set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing compliance suite in ALL mode...
call mvn test -Dtest=LinearAlgebraComplianceTest ^
    -Dorg.episteme.test.suite=all ^
    -Dorg.episteme.report.path=docs\LINEAR_ALGEBRA_COMPLIANCE_REPORT.md ^
    -Dorg.episteme.project.name="Episteme Linear Algebra" ^
    -Dsurefire.failIfNoSpecifiedTests=false ^
    -pl episteme-benchmarks -am

echo.
if exist "docs\LINEAR_ALGEBRA_COMPLIANCE_REPORT.md" (
    echo [SUCCESS] Verification complete. Report generated at docs\LINEAR_ALGEBRA_COMPLIANCE_REPORT.md
) else (
    echo [ERROR] Report generation failed. Check maven logs above.
)
endlocal
