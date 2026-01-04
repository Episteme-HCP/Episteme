#!/bin/bash

APP_CLASS="org.jscience.apps.engineering.SmartGridApp"

echo "Starting Smart Grid Balancer..."
mvn -f ../pom.xml exec:java -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
