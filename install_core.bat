@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"
mvn install -pl episteme-core -Dmaven.test.skip=true -Drevision=1.0.0-beta3
