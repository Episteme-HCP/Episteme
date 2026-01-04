#!/bin/bash

APP_CLASS="org.jscience.apps.social.MarketCrashApp"

echo "Starting Market Crash Predictor..."
mvn -f ../pom.xml exec:java -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
