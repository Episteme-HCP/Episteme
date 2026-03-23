#!/bin/bash
export JAVA_HOME=/home/silve/jdk-25.0.1
export PATH=$JAVA_HOME/bin:/usr/bin:/bin:$PATH
cd ~/Episteme
git reset --hard origin/main
git pull
mvn clean compile -pl episteme-benchmarks -am -DskipTests
MAVEN_OPTS="--add-modules jdk.incubator.vector" mvn exec:java -pl episteme-benchmarks -Dexec.mainClass="org.episteme.benchmarks.SystematicBenchmark" -Dexec.args="--run-all --domain Physics --iterations 1" -Pjcuda-linux-x86_64
