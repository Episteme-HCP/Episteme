#!/bin/bash
# scripts/build/build_jdk25.sh
# Unix script for compilation and installation under JDK 25 environment

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../../scripts/setup/env_setup.sh"

# Setup incubator and preview modules options for Maven compiler
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED"

# Install using revision property mapping
mvn install -pl episteme-benchmarks -am -Dmaven.test.skip=true -Drevision=1.0.0-beta3
