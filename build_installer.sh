#!/bin/bash

echo "==========================================="
echo "Episteme Suite Installer Builder"
echo "==========================================="

echo "[1/2] Building Runnable Fat JAR..."
# Use ./mvnw if available, else mvn
if [ -f "./mvnw" ]; then
    ./mvnw clean package -pl episteme-featured-apps -am -DskipTests
else
    mvn clean package -pl episteme-featured-apps -am -DskipTests
fi

if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

echo "[2/2] Generating Native Installer (App Image)..."
# Check if jpackage is available
if ! command -v jpackage &> /dev/null; then
    echo "Error: jpackage not found. Please ensure you are running JDK 14+."
    exit 1
fi

# Detect OS to adjust jpackage arguments if necessary (e.g. --mac-package-name)
# For now, sticking to the standard cross-platform args similar to the bat file

jpackage --name "EpistemeSuite" \
  --input episteme-featured-apps/target \
  --main-jar episteme-featured-apps-1.0.0-SNAPSHOT.jar \
  --main-class org.episteme.apps.Launcher \
  --type app-image \
  --dest installer \
  --description "Episteme Demos and Featured Apps" \
  --vendor "Silvere Martin-Michiellot"

if [ $? -eq 0 ]; then
    echo "==========================================="
    echo "Installer generated in: $(pwd)/installer/EpistemeSuite"
    echo "==========================================="
else
    echo "Installer generation failed."
    exit 1
fi
