#!/bin/bash
APP_CLASS=org.jscience.apps.chemistry.ChemicalStudio
LIB_DIR=launchers/lib
MODULE_PATH="jscience-featured-apps/target/classes:jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes"

echo "Starting Chemical Intelligence Studio..."
java --module-path "${LIB_DIR}/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml -cp "${MODULE_PATH}:${LIB_DIR}/*" ${APP_CLASS} "$@"
