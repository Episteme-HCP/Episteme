#!/bin/bash
# Launcher for JScience Master Control

echo "Starting JScience Master Control..."
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
mvn exec:java -pl jscience-featured-apps -Dexec.mainClass="org.jscience.core.ui.JScienceMasterControl" -Dexec.args="$@"
