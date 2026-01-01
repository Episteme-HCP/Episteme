#!/bin/bash
# Start a JScience Worker Node
echo "Starting JScience Worker Node..."

if [ ! -f "jscience-worker/target/jscience-worker-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Worker..."
    mvn clean package -pl jscience-worker -am -DskipTests
fi

echo "Launching Worker connected to localhost:50051..."
java -cp jscience-worker/target/jscience-worker-1.0.0-SNAPSHOT.jar:jscience-worker/target/lib/* org.jscience.worker.WorkerNode localhost 50051
