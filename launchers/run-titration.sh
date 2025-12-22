#!/bin/bash

APP_CLASS="org.jscience.apps.chemistry.TitrationApp"

echo "Starting Virtual Titration Lab..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
