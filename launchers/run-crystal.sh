#!/bin/bash

APP_CLASS="org.jscience.apps.chemistry.CrystalStructureApp"

echo "Starting Crystal Structure Explorer..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
