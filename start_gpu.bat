@echo off
echo Starting JScience in GPU Mode...
echo Note: Requires CUDA drivers installed.
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED -cp target/classes;target/test-classes -Dorg.jscience.compute.mode=GPU org.jscience.JScience
pause
