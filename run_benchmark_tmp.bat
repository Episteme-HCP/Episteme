@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-25
set PATH=%JAVA_HOME%\bin;%PATH%
set MAVEN_OPTS=--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED
mvn -X -o exec:java -pl episteme-benchmarks -Dexec.mainClass=org.episteme.benchmarks.cli.BenchmarkCLI -Dexec.args="%*" -Drevision=1.0.0-beta3 -Depisteme.native.skip.javacv=true -Depisteme.backend.native-javacv-media.disabled=true -Depisteme.backend.vlcj.disabled=true -Depisteme.backend.media.disabled=true -Depisteme.backend.vision.disabled=true -Depisteme.backend.gpu.disabled=true -Dorg.episteme.audit.exclude=VLCJ,JavaCV,Tarsos,Media,Vision,Audio,Video
