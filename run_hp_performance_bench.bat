@echo off
setlocal

echo ==========================================
echo Running High-Precision Performance Benchmark
echo ==========================================

set "DEPENDENCY_DIR=episteme-benchmarks\target\lib"
set "MODULE_PATH=episteme-benchmarks\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-native\target\classes"

if not exist "episteme-benchmarks\target\classes" (
    echo [INFO] Compiling modules...
    call mvn compile -pl episteme-benchmarks -am -DskipTests
)

if not exist "%DEPENDENCY_DIR%" (
    echo [INFO] Copying dependencies...
    call mvn dependency:copy-dependencies -pl episteme-benchmarks -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests
)

set "JVM_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo [INFO] Launching BenchmarkCLI...
java %JVM_OPTS% -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*" org.episteme.benchmarks.cli.BenchmarkCLI --run-all --domain "Linear Algebra" --dry-run --pdf --export-file docs/HIGH_PRECISION_PERFORMANCE_REPORT.json

echo.
echo Benchmark completed. View report at docs/HIGH_PRECISION_PERFORMANCE_REPORT.pdf
pause
endlocal
