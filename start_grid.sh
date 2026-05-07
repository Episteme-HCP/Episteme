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

# Start Episteme Grid with Docker Compose
echo "Starting Episteme Grid..."
echo ""
echo "This will start:"
echo "  - 1x Episteme Server (port 50051)"
echo "  - 5x Episteme Workers"
echo "  - Prometheus (port 9090)"
echo "  - Grafana (port 3000)"
echo ""

docker-compose up -d --build

echo ""
echo "Grid is starting! Check status with: docker-compose ps"
echo ""
echo "Grafana Dashboard: http://localhost:3000 (admin/episteme)"
echo "Prometheus:        http://localhost:9090"
echo "gRPC Server:       localhost:50051"
