@echo off
set JAR_PATH=jscience-benchmarks\target\jscience-benchmarks.jar

if not exist %JAR_PATH% (
    echo [INFO] Benchmark JAR not found. Building...
    call mvn package -pl jscience-benchmarks -DskipTests
)

echo.
echo [INFO] Starting JScience Benchmarking Suite...
java --add-modules jdk.incubator.vector -jar %JAR_PATH% %*
