#!/bin/bash
source /home/spinradnorman/.sdkman/bin/sdkman-init.sh
sdk use java 25.0.2-open
cd /home/spinradnorman/Episteme
mvn compile -pl episteme-native
# Use the full classpath and module flags for Panama FFM
mvn exec:java -pl episteme-native -Dexec.mainClass="org.episteme.nativ.mathematics.linearalgebra.backends.CudaDebug"
