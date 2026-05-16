@echo off
REM Use environment JAVA_HOME or system default java
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED
mvn install -pl episteme-benchmarks -am -Dmaven.test.skip=true -Drevision=1.0.0-beta3
