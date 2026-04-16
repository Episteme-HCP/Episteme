@echo off
setlocal
set "SCRIPT_DIR=%~dp0"
set "PATH=%SCRIPT_DIR%libs;%PATH%"
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

call mvn test -Dtest=LinearAlgebraComplianceTest ^
    -Dorg.episteme.test.suite=float ^
    -Dorg.episteme.include.provider=Episteme ^
    -Dorg.episteme.report.path=docs\FLOAT_STABILIZATION_DEBUG.md ^
    -Dsurefire.failIfNoSpecifiedTests=false ^
    -pl episteme-benchmarks -am
endlocal
