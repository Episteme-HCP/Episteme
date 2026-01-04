#!/bin/bash
# Launcher for Loader Verification

echo "Starting Loader Verification..."
mvn -f ../pom.xml compile org.codehaus.mojo:exec-maven-plugin:3.1.0:java -pl jscience-featured-apps "-Dexec.mainClass=org.jscience.apps.util.LoaderVerification"
