#!/bin/bash
# Launcher for JScience Master Control

APP_CLASS=org.jscience.ui.JScienceMasterControl
LIB_DIR="launchers/lib"
MODULE_PATH="jscience-featured-apps/target/classes:jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes"

echo "Starting JScience Master Control..."
java --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
     --module-path "${LIB_DIR}/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml \
     -cp "${MODULE_PATH}:${LIB_DIR}/*" ${APP_CLASS}
