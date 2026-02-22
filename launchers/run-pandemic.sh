#!/bin/bash

APP_CLASS="org.jscience.apps.biology.PandemicForecasterApp"
MODULE_PATH="../jscience-featured-apps/target/classes:../jscience-core/target/classes:../jscience-natural/target/classes:../jscience-social/target/classes:../jscience-mathematics/target/classes"

# Note: Adjust paths to JavaFX/Jackson libs for your system if not in ~/.m2
LIBS="$HOME/.m2/repository/org/openjfx/javafx-controls/17.0.2/javafx-controls-17.0.2-linux.jar:$HOME/.m2/repository/org/openjfx/javafx-graphics/17.0.2/javafx-graphics-17.0.2-linux.jar:$HOME/.m2/repository/org/openjfx/javafx-base/17.0.2/javafx-base-17.0.2-linux.jar"

echo "Starting Pandemic Forecaster..."
# Using exec:java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED via maven is often easier for cross-platform
mvn -f ../pom.xml exec:java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -pl jscience-featured-apps -Dexec.mainClass="$APP_CLASS"
