@echo off
REM run_universal_performance_audit.bat
REM Runs NORMAL and EXACT Linear Algebra Performance Audits.

echo ==========================================
echo Starting Universal Performance Audits
echo ==========================================

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo Redirecting to run_universal_audit.bat...
call run_universal_audit.bat

echo ==========================================
echo Performance Audits Completed.
echo JSON and PDF reports are in docs/benchmark-results/
echo ==========================================
pause
