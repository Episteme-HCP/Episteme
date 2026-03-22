#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/home/silve/Episteme/libs:/usr/local/cuda/lib64:$LD_LIBRARY_PATH
cd ~/Episteme
git fetch origin
git checkout main
git reset --hard origin/main
mvn clean package -DskipTests -Pjcuda-linux-x86_64
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --enable-preview -cp 'episteme-benchmarks/target/episteme-benchmarks-1.0.0-beta1.jar:episteme-core/target/episteme-core-1.0.0-beta1.jar:episteme-natural/target/episteme-natural-1.0.0-beta1.jar:episteme-social/target/episteme-social-1.0.0-beta1.jar:episteme-native/target/episteme-native-1.0.0-beta1.jar:episteme-client/target/episteme-client-1.0.0-beta1.jar:episteme-server/target/episteme-server-1.0.0-beta1.jar:episteme-benchmarks/target/lib/*' org.episteme.benchmarks.cli.BenchmarkCLI --run-all --domain 'Physics'
