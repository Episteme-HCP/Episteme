#!/bin/bash
# Launcher for JScience Dashboard

APP_CLASS=org.jscience.ui.JScienceDashboard
LIB_DIR=launchers\lib
MODULE_PATH="jscience-killer-apps/target/classes:jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes"

echo "Starting JScience Demos Suite..."
java --module-path "${LIB_DIR}/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "${MODULE_PATH}:${LIB_DIR}/*" ${APP_CLASS}
