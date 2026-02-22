#!/bin/bash
# Start the JScience Demos App (Client)
echo "Starting JScience Client..."

if [ ! -f "jscience-core/target/jscience-core-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Core..."
    mvn clean package -pl jscience-core -am -DskipTests
fi

echo "Launching App..."
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path jscience-core/target/classes:jscience-core/target/lib --add-modules javafx.controls,javafx.fxml -cp jscience-core/target/jscience-core-1.0.0-SNAPSHOT.jar org.jscience.ui.JScienceDemosApp "$@"
