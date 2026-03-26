@echo off
set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
if exist "%~dp0libs" set "PATH=%~dp0libs;%PATH%"

set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED
mvn.cmd clean test "-Dtest=HighPrecisionComplianceTest,NumericalCorrectnessTest,HighPrecisionPerformanceAudit" "-DskipTests=false" "-DfailIfNoTests=false" "-Dsurefire.failIfNoSpecifiedTests=false" "-pl" "episteme-benchmarks" "-am" "-Denforcer.skip=true"
