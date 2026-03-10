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

# Nettoyage des logs précédents
rm -f "$LOG_DIR/console.txt"

# Ensure directories are writable by the current user
chmod -R 777 "$LOG_DIR" "$RES_DIR" "$(pwd)/libs" 2>/dev/null || echo "[WARN] Could not chmod directories. If you see permission errors, try: sudo chown -R \$USER:\$USER ."

# Check for font libraries (required for PDF generation)
if ! dpkg -l | grep -q "libfontconfig1"; then
    echo "[WARN] libfontconfig1 not found. PDF generation might fail. Try: sudo apt-get update && sudo apt-get install -y libfontconfig1" | tee -a "$LOG_DIR/console.txt"
fi

echo "--- [1/2] Mise à jour et Compilation Locale ---" | tee -a "$LOG_DIR/console.txt"
# Gestion Git classique - Force sync
git fetch origin main | tee -a "$LOG_DIR/console.txt"
git reset --hard origin/main | tee -a "$LOG_DIR/console.txt"

# Compilation rapide (sans tests, sans copier les dépendances GUI inutiles)
export JAVA_HOME="/usr/lib/jvm/java-25-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"
export CUDA_PATH="/usr/local/cuda"

# Compilation C++ Vision
chmod +x build_vision.sh
./build_vision.sh 2>&1 | tee -a "$LOG_DIR/console.txt"
# Copy vision lib to shared libs directory
cp episteme-native/src/main/resources/linux-x86_64/libepisteme_vision.so libs/ 2>/dev/null

# Compilation JNI
cd episteme-jni
chmod +x compile_jni_linux.sh
./compile_jni_linux.sh 2>&1 | tee -a "$LOG_DIR/console.txt"
cd ..

# Compilation Java Maven
mvn install -DskipTests -Pheadless 2>&1 | tee -a "$LOG_DIR/console.txt"

# Debug: Vérifier la présence des classes natives
echo "[DEBUG] Classes natives générées :" | tee -a "$LOG_DIR/console.txt"
ls -R episteme-native/target/classes/org/episteme/nativ/mathematics/linearalgebra/backends 2>/dev/null | tee -a "$LOG_DIR/console.txt"

echo "--- [2/2] Exécution des Benchmarks (Native Linux) ---" | tee -a "$LOG_DIR/console.txt"
if [ "$#" -eq 0 ]; then
    BENCH_ARGS=(--cli --run-all --domain "Linear Algebra" --exclude-provider ND4J --pdf)
    echo "Aucun argument fourni. Utilisation des filtres par défaut : ${BENCH_ARGS[*]}" | tee -a "$LOG_DIR/console.txt"
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
./run-benchmarks.sh "${BENCH_ARGS[@]}" 2>&1 | tee -a "$LOG_DIR/console.txt"

echo "==========================================" | tee -a "$LOG_DIR/console.txt"
echo "Benchmark Session Complete" | tee -a "$LOG_DIR/console.txt"
echo "==========================================" | tee -a "$LOG_DIR/console.txt"
echo "Results exported to: $RES_DIR" | tee -a "$LOG_DIR/console.txt"
ls -lh "$RES_DIR" | tail -n 5 | tee -a "$LOG_DIR/console.txt"
echo "==========================================" | tee -a "$LOG_DIR/console.txt"

# Copy results to tmp/ for automatic retrieval by the user's script
echo "Copying results to $LOG_DIR for retrieval..."
cp "$RES_DIR"/* "$LOG_DIR/" 2>/dev/null
chmod -R 777 "$LOG_DIR"

echo "Terminé!" | tee -a "$LOG_DIR/console.txt"
