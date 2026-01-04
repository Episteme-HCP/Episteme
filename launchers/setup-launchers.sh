#!/bin/bash
# Setup script for JScience launcher dependencies

echo "Setting up launcher dependencies..."
echo "This script will download required JARs to the 'lib' directory."

cd ../jscience-featured-apps
mvn dependency:copy-dependencies "-DoutputDirectory=../launchers/lib" "-DincludeScope=runtime"
cd ../launchers

echo ""
echo "Setup complete. You can now run the launcher scripts."
