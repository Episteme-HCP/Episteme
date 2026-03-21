#!/bin/bash
export JAVA_HOME=~/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu/:$LD_LIBRARY_PATH

cd ~/Episteme/episteme-benchmarks
mvn test -Dtest=LinearAlgebraComplianceTest -DfailIfNoTests=false -Dorg.episteme.linearalgebra.compliance.report=true

# Copy the report to home for easy retrieval
cp target/mathematics/linearalgebra/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md ~/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md
