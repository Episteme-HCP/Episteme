#!/bin/bash
# Episteme Clean Build Script
# This script performs a full clean and install, bypassing tests and enforcer for speed.

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

mvn clean install -DskipTests -Denforcer.skip=true --no-transfer-progress
