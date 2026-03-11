#!/bin/bash
source /home/silve/.sdkman/bin/sdkman-init.sh
cd /home/spinradnorman/Episteme
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt -pl episteme-native
javac -cp $(cat cp.txt):target/classes Nd4jDiag.java
java -cp $(cat cp.txt):target/classes:. Nd4jDiag