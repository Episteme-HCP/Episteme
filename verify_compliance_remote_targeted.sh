#!/bin/bash
export JAVA_HOME=~/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/usr/local/cuda/lib64:$LD_LIBRARY_PATH
cd ~/Episteme
mvn test -pl episteme-benchmarks -Dtest=LinearAlgebraComplianceTest \
  -DargLine="--enable-preview --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED" \
  -Depisteme.backend.disable.grpc-math=true
