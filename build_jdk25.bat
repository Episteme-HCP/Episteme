@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-25
set PATH=%JAVA_HOME%\bin;%PATH%
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED
mvn install -pl episteme-benchmarks -am -Dmaven.test.skip=true -Drevision=1.0.0-beta3
