@echo off
setlocal
echo ==========================================
echo Running High-Precision Performance Report
echo ==========================================

set "JVM_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

if not exist "episteme-benchmarks\target\classes" (
    echo [INFO] Compiling modules...
    call mvn compile -pl episteme-benchmarks -am -DskipTests
)

if not exist "episteme-benchmarks\target\lib" (
    echo [INFO] Copying dependencies...
    call mvn dependency:copy-dependencies -pl episteme-benchmarks -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests
)

echo [INFO] Launching BenchmarkCLI for High-Precision Audit...
java %JVM_OPTS% -cp "episteme-benchmarks\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-native\target\classes;episteme-benchmarks\target\lib\*" org.episteme.benchmarks.cli.BenchmarkCLI --run hp-audit --pdf --export-file docs/HIGH_PRECISION_PERFORMANCE_REPORT.json

echo.
echo Performance report generated at docs/HIGH_PRECISION_PERFORMANCE_REPORT.pdf
pause
endlocal
