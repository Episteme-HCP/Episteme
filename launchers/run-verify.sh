#!/bin/bash
# Launcher for Loader Verification

echo "Starting Loader Verification..."
mvn -f ../pom.xml compile org.codehaus.mojo:exec-maven-plugin:3.1.0:java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -pl jscience-featured-apps "-Dexec.mainClass=org.jscience.apps.util.LoaderVerification"
