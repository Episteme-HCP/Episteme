#!/bin/bash
# JScience Optimized Launch Script
echo "Starting JScience with Optimized JVM Settings..."

# Check for Java
if ! command -v java &> /dev/null; then
    echo "Error: Java not found in PATH."
    exit 1
fi

# Run with optimizations
# Adjust memory (-Xmx) as needed for your system
# Note: Classpath uses ':' separator on Unix/Linux/macOS
java -server \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+ParallelRefProcEnabled \
     -XX:+UseStringDeduplication \
     -XX:+AlwaysPreTouch \
     -Xms2G -Xmx4G \
     -cp "jscience-natural/target/classes:jscience-core/target/classes:jscience-social/target/classes:jscience-ui/target/classes:jscience-killer-apps/target/classes:lib/*" \
     org.jscience.JScience
