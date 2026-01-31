#!/bin/bash
set -e

# Determine script location (ROOT)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
PROJECT_ROOT="$SCRIPT_DIR"
LAUNCHERS_DIR="$PROJECT_ROOT/launchers"
TARGET_DIR="$LAUNCHERS_DIR/packaged"
LIB_DIR="$TARGET_DIR/lib"

echo "Building project from $PROJECT_ROOT ..."
cd "$PROJECT_ROOT"
mvn clean install -DskipTests

echo "Cleaning target directory: $TARGET_DIR"
rm -rf "$TARGET_DIR"
mkdir -p "$LIB_DIR"

echo "Copying dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory="$LIB_DIR" -DincludeScope=runtime -DskipTests

echo "Copying project artifacts..."
# Find jars in submodules target/ (excluding sources/javadoc)
# We search in likely submodules or just depth 2
find . -maxdepth 3 -name "jscience-*.jar" -path "*/target/*" ! -name "*-sources.jar" ! -name "*-javadoc.jar" -exec cp {} "$TARGET_DIR" \;

echo "Generating launchers (Unix)..."
if [ -d "$LAUNCHERS_DIR" ]; then
    for SCRIPT in "$LAUNCHERS_DIR"/run-*.sh; do
        if [ -f "$SCRIPT" ]; then
            FILENAME=$(basename "$SCRIPT")
            APP_CLASS=$(grep "APP_CLASS=" "$SCRIPT" | head -n 1 | cut -d= -f2)
            
            if [ -n "$APP_CLASS" ]; then
                echo "Processing $FILENAME -> $APP_CLASS"
                NEW_SCRIPT="$TARGET_DIR/$FILENAME"
                
                cat > "$NEW_SCRIPT" <<EOF
#!/bin/bash
APP_CLASS=$APP_CLASS
LIB_DIR=lib
NATIVE_ARGS="--enable-native-access=ALL-UNNAMED --enable-preview"
CLASSPATH=".:*:lib/*"

echo "Starting \$APP_CLASS ..."
exec java \$NATIVE_ARGS -cp "\$CLASSPATH" \$APP_CLASS
EOF
                chmod +x "$NEW_SCRIPT"
            else
                echo "Skipping $FILENAME: APP_CLASS not found"
            fi
        fi
    done
else
    echo "Launchers directory not found: $LAUNCHERS_DIR"
fi

echo "Done. Packages available in $TARGET_DIR"
