#!/bin/bash
source "$(dirname "$0")/../scripts/setup/env_setup.sh"

# Start the Episteme Java MCP Server
echo "Starting Episteme Java Kernel..."
java -Djava.net.preferIPv4Stack=true \
     -XX:MaxDirectMemorySize=512M \
     -jar /app/server.jar > /app/server.log 2>&1 &

# Wait for the server to be ready
echo "Waiting for kernel to initialize..."
until curl -s http://localhost:8080/actuator/health | grep "UP" > /dev/null; do
  sleep 2
done
echo "Kernel is UP."

# Start the Gradio Playground
echo "Starting Agentic Playground..."
python3 /app/agent/playground.py

