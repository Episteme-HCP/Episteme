@echo off
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED
mvn test -Dtest=org.episteme.nativ.mathematics.analysis.MPFRTranscendentalTest -pl episteme-native -Dsurefire.useFile=false -Dorg.slf4j.simpleLogger.defaultLogLevel=info
