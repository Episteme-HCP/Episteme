#!/bin/bash

# Define paths
PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
TARGET_DIR="$PROJECT_ROOT/launchers/packaged"
LIB_DIR="$TARGET_DIR/lib"

echo "==========================================="
echo "JScience Project Packager"
echo "==========================================="

# 1. Build the project
echo "[1/4] Building project..."
mvn clean install -DskipTests -f "$PROJECT_ROOT/pom.xml"
if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

# 2. Prepare directories
echo "[2/4] Preparing directories..."
if [ -d "$TARGET_DIR" ]; then
    rm -rf "$TARGET_DIR"
fi
mkdir -p "$LIB_DIR"

# 3. Copy dependencies
echo "[3/4] Copying dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory="$LIB_DIR" -DincludeScope=runtime -DskipTests -f "$PROJECT_ROOT/pom.xml"
if [ $? -ne 0 ]; then
    echo "Copying dependencies failed."
    exit 1
fi

# 4. Copy project jars
echo "[4/4] Copying project jars..."
modules=("jscience-core" "jscience-natural" "jscience-social" "jscience-benchmarks" "jscience-featured-apps" "jscience-jni" "jscience-server" "jscience-worker" "jscience-client")
for mod in "${modules[@]}"; do
    if ls "$PROJECT_ROOT/$mod/target/"*.jar 1> /dev/null 2>&1; then
        cp "$PROJECT_ROOT/$mod/target/"*.jar "$LIB_DIR/"
    fi
done

# 5. Create Launch Scripts
echo "[5/4] Creating launch scripts..."
MAIN_CLASS="org.jscience.ui.JScienceMasterControl"

# run.bat
cat <<EOF > "$TARGET_DIR/run.bat"
@echo off
setlocal
set "CP=lib\*"
java -cp "%CP%" $MAIN_CLASS
endlocal
EOF

# run.sh
cat <<EOF > "$TARGET_DIR/run.sh"
#!/bin/bash
CP="lib/*"
java -cp "\$CP" $MAIN_CLASS
EOF
chmod +x "$TARGET_DIR/run.sh"

echo "Packaging complete at $TARGET_DIR"
echo "==========================================="
