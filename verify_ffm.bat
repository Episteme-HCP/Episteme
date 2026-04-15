@echo off
setlocal
echo [INFO] Initializing FFM Backend Verification (Windows)...

:: Setup library paths
set "SCRIPT_DIR=%~dp0"
set "PATH=%SCRIPT_DIR%libs;%SCRIPT_DIR%episteme-native\libs;%PATH%"

:: Enable Project Panama features
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED"

echo [INFO] Executing compliance suite for NativeFFM...
call mvn test -Dtest=LinearAlgebraComplianceTest ^
    -Dorg.episteme.include.provider=NativeFFM ^
    -Dorg.episteme.report.path=docs\FFM_VERIFICATION_REPORT.md ^
    -Dorg.episteme.project.name="Episteme FFM" ^
    -Dsurefire.failIfNoSpecifiedTests=false ^
    -pl episteme-benchmarks -am

echo.
if exist "docs\FFM_VERIFICATION_REPORT.md" (
    echo [SUCCESS] Verification complete. Report generated at docs\FFM_VERIFICATION_REPORT.md
) else (
    echo [ERROR] Report generation failed. Check maven logs above.
)
pause
