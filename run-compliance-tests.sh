#!/bin/bash

echo "Running Linear Algebra Compliance Tests..."

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"
mvn test -Dtest=LinearAlgebraComplianceTest -Dorg.jscience.project.name=Episteme -Dorg.jscience.report.path=../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md -pl jscience-native

echo ""
echo "Tests completed. View report at docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md"
