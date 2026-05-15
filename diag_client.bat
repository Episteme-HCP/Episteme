@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"
mvn compile -pl :episteme-client -am -Dmaven.test.skip=true -Drevision=1.0.0-beta3
