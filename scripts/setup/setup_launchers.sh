#!/bin/bash
# scripts/setup/setup_launchers.sh
# Setup launcher libraries by copying JavaFX and dependencies from target directory

set -e # exit on error

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo -e "\e[36m=== Episteme Launcher Libraries Setup ===\e[0m"
echo -e "\e[33mProject Root: $PROJECT_ROOT\e[0m"

FEATURED_APPS_DIR="$PROJECT_ROOT/episteme-featured-apps"
TARGET_LIB_DIR="$FEATURED_APPS_DIR/target/lib"

# Check if featured-apps target/lib exists. If not, trigger build.
if [ ! -d "$TARGET_LIB_DIR" ]; then
    echo -e "\e[33m[WARNING] Target dependencies not found at $TARGET_LIB_DIR\e[0m"
    echo -e "\e[33mPlease build the project first using 'mvn clean package -DskipTests' or run it now.\e[0m"
    
    echo -e "\e[36mRunning Maven package build to generate dependencies...\e[0m"
    cd "$PROJECT_ROOT"
    mvn clean package -pl episteme-featured-apps -am -DskipTests -Dmaven.test.skip=true
    
    if [ ! -d "$TARGET_LIB_DIR" ]; then
        echo -e "\e[31m[ERROR] Maven build failed to generate dependencies directory at $TARGET_LIB_DIR\e[0m"
        exit 1
    fi
fi

# --- 1. Populate episteme-featured-apps/launchers/libs (for Multi-Launcher apps) ---
echo -e "\n\e[36m--- Populating Multi-Launcher Libraries ---\e[0m"
LAUNCHERS_DIR="$FEATURED_APPS_DIR/launchers"
LAUNCHERS_LIBS_DIR="$LAUNCHERS_DIR/libs"
LAUNCHERS_LIBS_SUBDIR="$LAUNCHERS_LIBS_DIR/libs"
LAUNCHERS_JAVAFX_DIR="$LAUNCHERS_LIBS_DIR/javafx"

mkdir -p "$LAUNCHERS_LIBS_DIR" "$LAUNCHERS_LIBS_SUBDIR" "$LAUNCHERS_JAVAFX_DIR"

# Copy JavaFX Jars
echo -e "\e[32mCopying JavaFX Jars to $LAUNCHERS_JAVAFX_DIR...\e[0m"
cp "$TARGET_LIB_DIR"/javafx-* "$LAUNCHERS_JAVAFX_DIR/" 2>/dev/null || true
echo "  Copied JavaFX Jars."

# Copy other Jars to libs/libs
echo -e "\e[32mCopying dependencies to $LAUNCHERS_LIBS_SUBDIR...\e[0m"
find "$TARGET_LIB_DIR" -name "*.jar" ! -name "javafx-*" -exec cp {} "$LAUNCHERS_LIBS_SUBDIR/" \;
echo "  Copied dependencies."

# Copy project modules jars to libs as SNAPSHOT
echo -e "\e[32mCopying Episteme modules Jars...\e[0m"
MODULES=(
    "episteme-featured-apps"
    "episteme-core"
    "episteme-natural"
    "episteme-social"
    "episteme-demos"
)

for mod in "${MODULES[@]}"; do
    MOD_TARGET="$PROJECT_ROOT/$mod/target"
    if [ -d "$MOD_TARGET" ]; then
        # Find jar file matching the module name, excluding sources, javadoc, shaded
        JAR_FILE=$(find "$MOD_TARGET" -maxdepth 1 -name "$mod-*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" ! -name "*-shaded.jar" | head -n 1)
        if [ -n "$JAR_FILE" ]; then
            cp "$JAR_FILE" "$LAUNCHERS_LIBS_DIR/"
            # Also copy as SNAPSHOT version
            cp "$JAR_FILE" "$LAUNCHERS_LIBS_DIR/$mod-1.0.0-SNAPSHOT.jar"
        fi
    fi
done
echo "  Populated Episteme module JARs."

# --- 2. Populate launchers/lib (for root run_demos.sh) ---
echo -e "\n\e[36m--- Populating stand-alone launchers/lib ---\e[0m"
ROOT_LAUNCHERS_DIR="$PROJECT_ROOT/launchers"
ROOT_LAUNCHERS_LIB_DIR="$ROOT_LAUNCHERS_DIR/lib"
ROOT_LAUNCHERS_JAVAFX_DIR="$ROOT_LAUNCHERS_LIB_DIR/javafx"

mkdir -p "$ROOT_LAUNCHERS_DIR" "$ROOT_LAUNCHERS_LIB_DIR" "$ROOT_LAUNCHERS_JAVAFX_DIR"

# Copy JavaFX Jars
echo -e "\e[32mCopying JavaFX Jars to $ROOT_LAUNCHERS_JAVAFX_DIR...\e[0m"
cp "$TARGET_LIB_DIR"/javafx-* "$ROOT_LAUNCHERS_JAVAFX_DIR/" 2>/dev/null || true
echo "  Copied JavaFX Jars."

# Copy other Jars to launchers/lib
echo -e "\e[32mCopying dependencies directly to $ROOT_LAUNCHERS_LIB_DIR...\e[0m"
find "$TARGET_LIB_DIR" -name "*.jar" ! -name "javafx-*" -exec cp {} "$ROOT_LAUNCHERS_LIB_DIR/" \;
echo "  Copied dependencies."

# Copy project modules jars directly to launchers/lib as SNAPSHOT
echo -e "\e[32mCopying Episteme modules Jars...\e[0m"
for mod in "${MODULES[@]}"; do
    MOD_TARGET="$PROJECT_ROOT/$mod/target"
    if [ -d "$MOD_TARGET" ]; then
        JAR_FILE=$(find "$MOD_TARGET" -maxdepth 1 -name "$mod-*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" ! -name "*-shaded.jar" | head -n 1)
        if [ -n "$JAR_FILE" ]; then
            cp "$JAR_FILE" "$ROOT_LAUNCHERS_LIB_DIR/"
            # Also copy as SNAPSHOT version
            cp "$JAR_FILE" "$ROOT_LAUNCHERS_LIB_DIR/$mod-1.0.0-SNAPSHOT.jar"
        fi
    fi
done
echo "  Populated Episteme module JARs."

# --- 3. Path correction in batch launchers for development mode classpath (Windows batch scripts helper) ---
echo -e "\n\e[36m--- Correcting relative development paths in batch launchers ---\e[0m"
for bat_file in "$LAUNCHERS_DIR"/*.bat; do
    if [ -f "$bat_file" ]; then
        if grep -q "%~dp0\.\.\\episteme-" "$bat_file"; then
            echo -e "  Correcting paths in: \e[33m$(basename "$bat_file")\e[0m"
            sed -i 's/%~dp0\.\.\\episteme-/%~dp0..\\..\\episteme-/g' "$bat_file"
        fi
    fi
done

echo -e "\n\e[32mSetup complete! All launcher libraries are fully populated and standalone runs will now work.\e[0m"
