#!/bin/bash
source "$(dirname "$0")/../../scripts/setup/env_setup.sh"

# Environment Setup

cd "$(dirname "$0")/.."
APP_CLASS=org.episteme.apps.apps.engineering.SmartGridApp
LIB_DIR=launchers/libs/libs
MODULES_DIR=launchers/libs
MODULE_PATH="${MODULES_DIR}/episteme-featured-apps-1.0.0-SNAPSHOT.jar:${MODULES_DIR}/episteme-core-1.0.0-SNAPSHOT.jar:${MODULES_DIR}/episteme-natural-1.0.0-SNAPSHOT.jar:${MODULES_DIR}/episteme-social-1.0.0-SNAPSHOT.jar"

echo "Starting Smart Grid Balancer..."
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --module-path "$(dirname "$0")/libs/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "${MODULE_PATH}:${LIB_DIR}/*" ${APP_CLASS} "$@"

