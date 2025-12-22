#!/bin/bash

APP_CLASS="org.jscience.apps.physics.relativity.RelativisticFlightApp"

echo "Starting Relativistic Flight Sim..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
