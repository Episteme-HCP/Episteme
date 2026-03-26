@echo off
set "MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
mvn compile exec:java -pl episteme-benchmarks -Dexec.mainClass=org.episteme.benchmarks.cli.BenchmarkCLI -Dexec.args="--domain 'Linear Algebra' --dry-run --pdf --export-file ../docs/HP_REPORT.json"
