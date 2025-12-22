#!/bin/bash

APP_CLASS="org.jscience.apps.biology.CrisprDesignApp"

echo "Starting CRISPR Target Finder..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
