@echo off
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED
mvn.cmd test -pl episteme-benchmarks -am "-Dtest=HighPrecisionComplianceTest,NumericalCorrectnessTest" -DskipTests=false -DfailIfNoTests=false -Dsurefire.failIfNoSpecifiedTests=false -Denforcer.skip=true --no-transfer-progress
