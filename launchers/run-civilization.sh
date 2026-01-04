#!/bin/bash

APP_CLASS="org.jscience.apps.social.CivilizationApp"

echo "Starting Civilization Collapse Model..."
mvn -f ../pom.xml exec:java -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
