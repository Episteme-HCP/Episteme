#!/bin/bash
# JScience Spin Valve Simulator Launcher - Unix/Linux/macOS
# Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LIB_DIR="$SCRIPT_DIR/../lib"
JAR_FILE="$SCRIPT_DIR/../jscience-featured-apps-1.0-SNAPSHOT.jar"

# Find Java
if [ -n "$JAVA_HOME" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA="java"
fi

# Build classpath
CP="$JAR_FILE"
for jar in "$LIB_DIR"/*.jar; do
    CP="$CP:$jar"
done

# JavaFX modules
JFX_MODULES="javafx.controls,javafx.fxml,javafx.graphics,javafx.swing"

# Launch
echo "Starting JScience Spin Valve Simulator..."
exec "$JAVA" --module-path "$LIB_DIR" --add-modules "$JFX_MODULES" -cp "$CP" \
    org.jscience.apps.physics.spintronics.SpinValveApp "$@"
