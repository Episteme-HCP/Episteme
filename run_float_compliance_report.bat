@echo off
setlocal
echo [INFO] Initializing Float Linear Algebra Compliance Report (Windows)...

:: Setup library paths
set "SCRIPT_DIR=%~dp0"
set "PATH=%SCRIPT_DIR%libs;%SCRIPT_DIR%episteme-native\libs;%PATH%"

:: Enable Project Panama features and Vector API
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Executing compliance suite in FLOAT mode...
call mvn test -Dtest=LinearAlgebraComplianceTest ^
    -Dorg.episteme.test.suite=float ^
    -Dorg.episteme.include.provider="FFM,CPU (Dense),CPU (Sparse),CUDA,OpenCL,SIMD,CPU-BLAS" ^
    -Dorg.episteme.report.path=docs\FLOAT_ALGEBRA_COMPLIANCE_REPORT.md ^
    -Dorg.episteme.project.name="Episteme Float Algebra" ^
    -Dsurefire.failIfNoSpecifiedTests=false ^
    -pl episteme-benchmarks -am

echo.
if exist "docs\FLOAT_ALGEBRA_COMPLIANCE_REPORT.md" (
    echo [SUCCESS] Verification complete. Report generated at docs\FLOAT_ALGEBRA_COMPLIANCE_REPORT.md
) else (
    echo [ERROR] Report generation failed. Check maven logs above.
)
pause
endlocal
