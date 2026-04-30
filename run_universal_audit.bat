@echo off
REM run_universal_audit.bat
REM Launches the Universal Multimodal Performance Audit (Single JVM)

echo ==========================================
echo Starting Universal Multimodal Performance Audit
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

call mvn clean test -pl episteme-benchmarks -am -Dtest=UniversalMultimodalAudit -Dsurefire.failIfNoSpecifiedTests=false

echo ==========================================
echo Audit Completed.
echo Reports generated in docs/benchmark-results/
echo ==========================================

