#!/bin/bash
# Script to run compliance tests on the VM
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
cd ~/Episteme
echo "Syncing latest code..."
git fetch origin main
git reset --hard origin/main
echo "Running Linear Algebra Compliance Test..."
mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest -DfailIfNoTests=false -DargLine="--enable-preview --add-modules jdk.incubator.vector"
