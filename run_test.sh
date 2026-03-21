#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
cd /home/silve/Episteme/episteme-benchmarks
nohup mvn test -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.include.provider="CUDA Sparse" > /home/silve/Episteme/final_test_run.log 2>&1 &
echo "[DEBUG] Test run started in background. Check /home/silve/Episteme/final_test_run.log for progress."
