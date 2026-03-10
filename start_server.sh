#!/bin/bash

# VLC and Native Libs Setup
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LIBS_DIR="$SCRIPT_DIR/libs"
if [ ! -d "$LIBS_DIR" ]; then LIBS_DIR="$SCRIPT_DIR/../libs"; fi
if [ -d "$LIBS_DIR" ]; then
    echo "[INFO] Adding libs/ to library path..."
    export LD_LIBRARY_PATH="$LIBS_DIR:$LD_LIBRARY_PATH"
    export DYLD_LIBRARY_PATH="$LIBS_DIR:$DYLD_LIBRARY_PATH"
fi
if [ -d "/usr/lib/vlc" ]; then
    export LD_LIBRARY_PATH="/usr/lib/vlc:$LD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/usr/lib/vlc/plugins"
fi
if [ -d "/Applications/VLC.app/Contents/MacOS/lib" ]; then
    export DYLD_LIBRARY_PATH="/Applications/VLC.app/Contents/MacOS/lib:$DYLD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/Applications/VLC.app/Contents/MacOS/plugins"
fi

# Start the Episteme Distributed Server
echo "Starting Episteme Server..."

if [ ! -f "episteme-server/target/episteme-server-1.0.0-SNAPSHOT.jar" ]; then
    echo "Building Server..."
    mvn clean package -pl episteme-server -am -DskipTests
fi

java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp episteme-server/target/episteme-server-1.0.0-SNAPSHOT.jar:episteme-server/target/lib/* org.episteme.server.JscienceServer
