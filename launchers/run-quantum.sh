#!/bin/bash

APP_CLASS="org.jscience.apps.physics.QuantumCircuitApp"

echo "Starting Quantum Circuit Simulator..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
