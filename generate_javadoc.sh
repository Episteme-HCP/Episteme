#!/bin/bash
set -e

# Ensure we are in the script's directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

echo "============================================"
echo "  JScience Javadoc Generator"
echo "============================================"
echo ""
echo "This script will regenerate the Javadoc for the entire JScience project."
echo ""

# Optional: Clean old javadoc
if [ -d "javadoc" ]; then
    echo "Cleaning old javadoc folder..."
    rm -rf javadoc
fi

echo ""
echo "Running Maven Javadoc Aggregate..."
echo "(This may take a few minutes)"
echo ""

# We run compile first to ensure any generated sources (like Protobuf) are available
mvn compile javadoc:aggregate -DreportOutputDirectory="$DIR" -DdestDir=javadoc -Dnotimestamp=true

echo ""
echo "============================================"
echo "  SUCCESS!"
echo "  Javadoc is available in: $DIR/javadoc"
echo "============================================"
