#!/bin/bash
# Episteme JNI Linux Compilation Script
# Compiles libepisteme-jni.so for Linux

if [ -z "$JAVA_HOME" ]; then
    echo "ERROR: JAVA_HOME is not set."
    exit 1
fi

SOURCE_DIR="episteme-jni/src/main/cpp"
OUTPUT_DIR="episteme-jni/libs"
mkdir -p "$OUTPUT_DIR"

echo "Compiling episteme-jni for Linux..."

g++ -O3 -shared -fPIC \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/linux" \
    -I"$SOURCE_DIR" \
    "$SOURCE_DIR/episteme_jni.cpp" \
    -o "$OUTPUT_DIR/libepisteme-jni.so"

if [ $? -eq 0 ]; then
    echo "[SUCCESS] libepisteme-jni.so created in $OUTPUT_DIR"
else
    echo "[FAILURE] Compilation failed"
    exit 1
fi
