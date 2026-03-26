#!/bin/bash
echo "=========================================="
echo "Running High-Precision Performance Report"
echo "=========================================="

export JVM_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

if [ ! -d "episteme-benchmarks/target/classes" ]; then
    echo "[INFO] Compiling modules..."
    mvn compile -pl episteme-benchmarks -am -DskipTests
fi

if [ ! -d "episteme-benchmarks/target/lib" ]; then
    echo "[INFO] Copying dependencies..."
    mvn dependency:copy-dependencies -pl episteme-benchmarks -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests
fi

echo "[INFO] Launching BenchmarkCLI for High-Precision Audit..."
java $JVM_OPTS -cp "episteme-benchmarks/target/classes:episteme-core/target/classes:episteme-natural/target/classes:episteme-native/target/classes:episteme-benchmarks/target/lib/*" org.episteme.benchmarks.cli.BenchmarkCLI --run hp-audit --pdf --export-file docs/HIGH_PRECISION_PERFORMANCE_REPORT.json

echo ""
echo "Performance report generated at docs/HIGH_PRECISION_PERFORMANCE_REPORT.pdf"
