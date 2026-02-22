#!/bin/bash

APP_CLASS="org.jscience.apps.social.CivilizationApp"

echo "Starting Civilization Collapse Model..."
mvn -f ../pom.xml exec:java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
