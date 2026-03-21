@echo off

rem --- Native Libraries Setup ---
if exist "C:\Program Files\VideoLAN\VLC" (
    echo [INFO] Adding VLC to PATH...
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)
if exist "%~dp0libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0libs;%PATH%"
)
if exist "%~dp0..\libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0..\libs;%PATH%"
)

setlocal

echo Running Linear Algebra Compliance Tests...

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -Depisteme.backend.disable.grpc-math=true
mvn.cmd clean test "-Dtest=LinearAlgebraComplianceTest" "-DskipTests=false" "-Dsurefire.failIfNoSpecifiedTests=false" "-Dorg.episteme.project.name=Episteme" "-Dorg.episteme.report.path=../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md" "-pl" "episteme-benchmarks" "-am" "-Denforcer.skip=true"

echo.
echo Tests completed. View report at docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md
endlocal
