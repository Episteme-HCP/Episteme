@echo off
set CP=episteme-benchmarks\target\classes;episteme-native\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-benchmarks\target\lib\*

java --enable-preview --enable-native-access=ALL-UNNAMED -cp "%CP%" org.episteme.benchmarks.BackendDiagnostic
