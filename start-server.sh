#!/bin/bash
# Start the JScience Distributed Server
echo "Starting JScience Server..."

if [ ! -f "jscience-server/target/jscience-server-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Server..."
    mvn clean package -pl jscience-server -am -DskipTests
fi

java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp jscience-server/target/jscience-server-1.0.0-SNAPSHOT.jar:jscience-server/target/lib/* org.jscience.server.JscienceServer
