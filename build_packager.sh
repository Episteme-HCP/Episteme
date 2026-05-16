#!/bin/bash
set -e

# Determine script location (ROOT)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
PROJECT_ROOT="$SCRIPT_DIR"
LAUNCHERS_DIR="$PROJECT_ROOT/launchers"
TARGET_DIR="$LAUNCHERS_DIR/packaged"

echo "Building project from $PROJECT_ROOT ..."
cd "$PROJECT_ROOT"
mvn clean install -DskipTests

echo "Cleaning target directory: $TARGET_DIR"
rm -rf "$TARGET_DIR"
mkdir -p "$TARGET_DIR"

echo "Copying artifacts (Thin Jars only)..."

MODULES=(
    "episteme-core" 
    "episteme-natural" 
    "episteme-social" 
    "episteme-native" 
    "episteme-featured-apps" 
    "episteme-server"
    "episteme-client"
    "episteme-database"
    "episteme-worker"
    "episteme-benchmarks"
    "episteme-jni"
)

for MOD in "${MODULES[@]}"; do
    MOD_TARGET="$PROJECT_ROOT/$MOD/target"
    if [ -d "$MOD_TARGET" ]; then
        # Search for original-X.jar (thin) or X.jar (excluding shaded/sources/javadoc)
        
        # 1. Try original-
        ORIGINAL_JAR=$(find "$MOD_TARGET" -maxdepth 1 -name "original-$MOD-*.jar" | head -n 1)
        
        if [ -n "$ORIGINAL_JAR" ]; then
            FILENAME=$(basename "$ORIGINAL_JAR")
            DEST_NAME=${FILENAME#"original-"}
            echo "Copying $FILENAME as $DEST_NAME (Thin)"
            cp "$ORIGINAL_JAR" "$TARGET_DIR/$DEST_NAME"
        else
            # 2. Try the normal jar, excluding suffixes
            # We look for the jar that doesn't have -shaded, -sources, -javadoc
            NORMAL_JAR=$(find "$MOD_TARGET" -maxdepth 1 -name "$MOD-*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" ! -name "*-shaded.jar" | head -n 1)
            
            if [ -n "$NORMAL_JAR" ]; then
                echo "Copying $(basename "$NORMAL_JAR") (Thin)"
                cp "$NORMAL_JAR" "$TARGET_DIR/"
            else
                echo "No valid thin jar found for $MOD"
            fi
        fi
    else
        echo "Module target not found: $MOD"
    fi
done

echo "Packaging complete. Handled thin versions for featured-apps and worker."
echo "Target: $TARGET_DIR"
