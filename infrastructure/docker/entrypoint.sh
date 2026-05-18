#!/bin/bash

# Setup library paths for Java native bindings
export LD_LIBRARY_PATH="/app/libs:/usr/local/lib:$LD_LIBRARY_PATH"

# Start the Episteme Java MCP Server
echo "Starting Episteme Java Kernel..."
java --add-modules jdk.incubator.vector \
     --enable-native-access=ALL-UNNAMED \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.util=ALL-UNNAMED \
     --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
     -Djava.net.preferIPv4Stack=true \
     -XX:MaxDirectMemorySize=512M \
     -jar /app/server.jar &

# Wait for the server to be ready
echo "Waiting for kernel to initialize..."
until curl -s http://localhost:8080/actuator/health | grep "UP" > /dev/null; do
  sleep 2
done
echo "Kernel is UP."

# Start the Gradio Playground
echo "Starting Agentic Playground..."
python3 /app/agent/playground.py
