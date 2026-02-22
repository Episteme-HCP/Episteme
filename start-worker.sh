#!/bin/bash
# Start a JScience Worker Node
echo "Starting JScience Worker Node..."

if [ ! -f "jscience-worker/target/jscience-worker-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Worker..."
    mvn clean package -pl jscience-worker -am -DskipTests
fi

echo "Launching Worker connected to localhost:50051..."
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp jscience-worker/target/jscience-worker-1.0.0-SNAPSHOT.jar:jscience-worker/target/lib/* org.jscience.worker.WorkerNode localhost 50051
