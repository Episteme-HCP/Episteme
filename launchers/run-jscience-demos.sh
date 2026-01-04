#!/bin/bash
# Launcher for JScience Demo Suite

APP_CLASS=org.jscience.ui.JScienceDemoApp
LIB_DIR=lib
MODULE_PATH="../jscience-featured-apps/target/classes:../jscience-core/target/classes:../jscience-natural/target/classes:../jscience-social/target/classes:../jscience-mathematics/target/classes"

echo "Starting JScience Demo Suite..."
java --module-path "${LIB_DIR}/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "${MODULE_PATH}:${LIB_DIR}/*" ${APP_CLASS}
