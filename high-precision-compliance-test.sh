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


echo "Running High-Precision Arithmetic Compliance Tests..."

# Run compliance tests with native access enabled and libs in path
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$(pwd)/episteme-native/libs:$(pwd)/episteme-jni/libs
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
mvn test -Dtest=HighPrecisionComplianceTest -Dorg.episteme.project.name=Episteme -Dorg.episteme.report.path=../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md -pl episteme-core

echo ""
echo "Tests completed. View report at docs/HIGH_PRECISION_COMPLIANCE_REPORT.md"
