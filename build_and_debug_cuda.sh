#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu/:$LD_LIBRARY_PATH

echo "[DEBUG] Starting build of episteme-native..."
cd /home/silve/Episteme/episteme-native
mvn clean install -DskipTests -q

if [ $? -eq 0 ]; then
    echo "[DEBUG] Build successful. Running targeted compliance test for CUDA Sparse using nohup..."
    cd /home/silve/Episteme/episteme-benchmarks
    nohup mvn test -Dtest=LinearAlgebraComplianceTest -Dorg.episteme.include.provider="CUDA Sparse" > /home/silve/Episteme/targeted_report_run.log 2>&1 &
    echo "[DEBUG] Test run started in background (nohup). Check /home/silve/Episteme/targeted_report_run.log for progress."
else
    echo "[DEBUG] Build FAILED!"
    exit 1
fi
