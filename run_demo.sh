#!/bin/bash
# Launcher for JScience Demo App
echo "Starting JScience Demo..."
mvn exec:java -pl jscience-natural -Dexec.mainClass="org.jscience.ui.JScienceDemoApp"
