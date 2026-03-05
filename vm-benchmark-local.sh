#!/bin/bash
# Episteme VM Native Local Execution Script
# Ce script compile et lance les benchmarks directement sur la VM Linux
# SANS passer par Docker, pour des itérations ultra-rapides.

# Configuration
LOG_DIR="$(pwd)/tmp"
RES_DIR="$(pwd)/docs/benchmark-results"
mkdir -p "$LOG_DIR"
mkdir -p "$RES_DIR"
mkdir -p "$(pwd)/libs"

# Ensure directories are writable by the current user
chmod -R 777 "$LOG_DIR" "$RES_DIR" "$(pwd)/libs" 2>/dev/null || echo "[WARN] Could not chmod directories. If you see permission errors, try: sudo chown -R \$USER:\$USER ."

echo "--- [1/2] Mise à jour et Compilation Locale ---"
# Gestion Git classique
git fetch origin main
git checkout main
git pull origin main

# Compilation rapide (sans tests, sans copier les dépendances GUI inutiles)
export JAVA_HOME="/usr/lib/jvm/java-25-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"
export CUDA_PATH="/usr/local/cuda"

# Compilation C++ Vision
chmod +x build_vision.sh
./build_vision.sh
# Copy vision lib to shared libs directory
cp episteme-native/src/main/resources/linux-x86_64/libepisteme_vision.so libs/

# Compilation JNI
cd episteme-jni
chmod +x compile_jni_linux.sh
./compile_jni_linux.sh
cd ..

# Compilation Java Maven
mvn clean install -DskipTests -Pheadless

echo "--- [2/2] Exécution des Benchmarks (Native Linux) ---"
if [ "$#" -eq 0 ]; then
    BENCH_ARGS=(--cli --run-all --domain "Linear Algebra" --exclude-provider ND4J --pdf)
    echo "Aucun argument fourni. Utilisation des filtres par défaut : ${BENCH_ARGS[*]}"
else
    BENCH_ARGS=("$@")
    HAS_RUN_ALL=false
    for arg in "${BENCH_ARGS[@]}"; do
        if [[ "$arg" == "--run-all" || "$arg" == "--help" || "$arg" == "--diagnostic" ]]; then
            HAS_RUN_ALL=true
            break
        fi
    done
    
    if [ "$HAS_RUN_ALL" = false ]; then
        BENCH_ARGS=(--run-all "${BENCH_ARGS[@]}")
    fi
fi

# Variables environnement requises pour le natif
export LD_LIBRARY_PATH="$(pwd)/libs:/usr/local/cuda/lib64:/usr/lib/x86_64-linux-gnu:${LD_LIBRARY_PATH}"

# On exécute run-benchmarks.sh en local avec capture de la console
chmod +x run-benchmarks.sh
./run-benchmarks.sh "${BENCH_ARGS[@]}" 2>&1 | tee "$LOG_DIR/console.txt"

echo "Terminé! Résultats dans $RES_DIR"
