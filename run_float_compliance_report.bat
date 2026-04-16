@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-25
set MAVEN_OPTS=--enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector
set JAVA_OPTS=--enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.vector

mvn clean test -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.test.suite=FLOAT -Dorg.episteme.report.path=docs\FLOAT_COMPLIANCE_REPORT.md -Dsurefire.failIfNoSpecifiedTests=false -pl episteme-benchmarks -am
