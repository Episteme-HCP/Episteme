@echo off
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED

echo [1/2] Auditing FAST Mode...
call mvn test -pl episteme-benchmarks -am "-Drevision=1.0.0-beta2" -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=fast -Dorg.episteme.test.provider.exclude=cuda,opencl -Dorg.episteme.native.cuda.disabled=true -Dorg.episteme.native.opencl.disabled=true -Dsurefire.failIfNoSpecifiedTests=false

echo [2/2] Auditing NORMAL Mode...
call mvn test -pl episteme-benchmarks -am "-Drevision=1.0.0-beta2" -Dtest=org.episteme.benchmarks.test.audit.LinearAlgebraComplianceTest -Dorg.episteme.test.precision=normal -Dorg.episteme.test.provider.exclude=cuda,opencl -Dorg.episteme.native.cuda.disabled=true -Dorg.episteme.native.opencl.disabled=true -Dsurefire.failIfNoSpecifiedTests=false
