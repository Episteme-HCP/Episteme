#!/bin/bash
APP_CLASS="org.jscience.apps.physics.spintronics.SpinValveApp"
LIB_DIR="lib"
# Classpath for development environment (target/classes)
CLASSPATH_DIRS="../jscience-featured-apps/target/classes:../jscience-core/target/classes:../jscience-natural/target/classes:../jscience-social/target/classes:../jscience-benchmarks/target/classes"

echo "Starting JScience Spin Valve Simulator..."
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "$LIB_DIR/javafx" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.swing -cp "$CLASSPATH_DIRS:$LIB_DIR/*" $APP_CLASS "$@"
