#!/bin/bash

APP_CLASS="org.jscience.apps.social.MarketCrashApp"

echo "Starting Market Crash Predictor..."
mvn -f ../pom.xml exec:java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
