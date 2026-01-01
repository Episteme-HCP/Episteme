#!/bin/bash
# Start the JScience Demos App (Client)
echo "Starting JScience Client..."

if [ ! -f "jscience-core/target/jscience-core-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Core..."
    mvn clean package -pl jscience-core -am -DskipTests
fi

echo "Launching App..."
java --module-path jscience-core/target/classes:jscience-core/target/lib --add-modules javafx.controls,javafx.fxml -cp jscience-core/target/jscience-core-1.0.0-SNAPSHOT.jar org.jscience.ui.JScienceDemosApp
