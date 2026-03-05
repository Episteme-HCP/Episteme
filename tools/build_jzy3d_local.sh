#!/bin/bash
set -e

# Setup clean working directory
WORK_DIR="/tmp/jzy3d-build"
rm -rf "$WORK_DIR"
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"

echo "[1/4] Cloning Jzy3d Repository..."
git clone https://github.com/jzy3d/jzy3d-api.git
cd jzy3d-api

echo "[2/4] Checking out version 2.2.2..."
git checkout jzy3d-all-2.2.2

echo "[3/4] Building and installing Jzy3d 2.2.2 to local Maven repository..."
# Skipping tests to accelerate the build of the GUI library
mvn clean install -DskipTests

echo "[4/4] Successfully installed Jzy3d 2.2.2 locally!"
echo "You can now reference org.jzy3d:jzy3d-native-jogl-[awt/swing] in your POMs."
