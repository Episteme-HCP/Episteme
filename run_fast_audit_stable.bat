@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED
mvn test -pl episteme-benchmarks -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=fast -Dsurefire.failIfNoSpecifiedTests=false -Drevision=1.0.0-beta3 -Dorg.episteme.audit.exclude=jblas,mpfr,NativeMPFR
