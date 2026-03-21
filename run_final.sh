#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu/:$LD_LIBRARY_PATH

echo "[DEBUG] Starting build of episteme-native..."
cd /home/silve/Episteme/episteme-native
mvn clean install -DskipTests -q

if [ $? -eq 0 ]; then
    echo "[DEBUG] Build successful. Starting targeted compliance test..."
    cd /home/silve/Episteme/episteme-benchmarks
    # Run targeted compliance test for CUDA Sparse
    mvn test -Dtest=LinearAlgebraComplianceTest \
        -Dorg.episteme.include.provider="CUDA Sparse" \
        -Dorg.episteme.exclude.provider="ND4J,Colt,MPFR,OpenCL,Generic" \
        -DargLine="--add-modules=jdk.incubator.vector --enable-native-access=ALL-UNNAMED" \
        > /home/silve/Episteme/final_test_run.log 2>&1
    echo "[DEBUG] Test run complete. Check final_test_run.log"
else
    echo "[DEBUG] Build FAILED!"
    exit 1
fi
