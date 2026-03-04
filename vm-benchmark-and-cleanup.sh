#!/bin/bash
# Episteme VM Maintenance & Benchmark Script
# - Nettoie l'espace disque (Docker + Maven)
# - Met à jour le code (Git pull avec gestion des conflits)
# - Construit l'image GPU optimisée
# - Lance les benchmarks avec accès GPU

# --- [0/4] Configuration ---
# On définit les dossiers de travail en premier pour éviter les erreurs de variable vide
LOG_DIR="$(pwd)/tmp"
RES_DIR="$(pwd)/docs/benchmark-results"
mkdir -p "$LOG_DIR"
mkdir -p "$RES_DIR"

echo "--- [1/4] État de l'espace disque & Nettoyage ---"
df -h / | grep /
# Nettoyage Docker (Images orphelines, containers arrêtés, cache inutilisé)
docker system prune -f --volumes
echo "Après nettoyage :"
df -h / | grep /

echo "--- [2/4] Mise à jour du code (Git) ---"
# Gestion des conflits locaux
GIT_CHANGES=$(git status --porcelain)
if [ -n "$GIT_CHANGES" ]; then
    echo "Changements locaux détectés. Stash temporaire..."
    git stash
fi

# Store the hash of the current script to detect updates
SCRIPT_HASH_BEFORE=$(md5sum "$0" | cut -d' ' -f1)

# Pull depuis la branche courante (main ou master)
CURRENT_BRANCH=$(git branch --show-current)
echo "Mise à jour depuis la branche : $CURRENT_BRANCH"
git pull origin "$CURRENT_BRANCH"

SCRIPT_HASH_AFTER=$(md5sum "$0" | cut -d' ' -f1)

if [ -n "$GIT_CHANGES" ]; then
    echo "Réapplication des changements locaux..."
    git stash pop
fi

# Self-restart if the script was updated
if [ "$SCRIPT_HASH_BEFORE" != "$SCRIPT_HASH_AFTER" ]; then
    echo "Le script de maintenance a été mis à jour via Git. Redémarrage immédiat..."
    exec "$0" "$@"
    exit 0
fi

echo "--- [3/4] Construction de l'image GPU (Logging vers $LOG_DIR/docker_build.log) ---"
# On force BuildKit pour le cache intelligent
export DOCKER_BUILDKIT=1
docker build --progress=plain -t episteme-gpu -f docker/Dockerfile.gpu . 2>&1 | tee "$LOG_DIR/docker_build.log"

echo "--- [4/4] Lancement des Benchmarks ---"
echo "Exécution des diagnostics..."
docker run --rm --gpus all episteme-gpu ./run-diagnostic.sh > "$LOG_DIR/diagnostic_output.txt" 2>&1

# Gestion des arguments par défaut
BENCH_ARGS="$@"
if [ -z "$BENCH_ARGS" ]; then
    BENCH_ARGS="--run-all --domain \"Linear Algebra\" --exclude-provider ND4J --pdf"
    echo "Aucun argument fourni. Utilisation des filtres par défaut : $BENCH_ARGS"
else
    # Si des arguments sont fournis mais que --run-all manque, on le rajoute pour éviter l'erreur "Nothing to run"
    if [[ ! "$BENCH_ARGS" =~ "--run-all" ]] && [[ ! "$BENCH_ARGS" =~ "--help" ]] && [[ ! "$BENCH_ARGS" =~ "--diagnostic" ]]; then
        BENCH_ARGS="--run-all $BENCH_ARGS"
    fi
fi

echo "Lancement des benchmarks : $BENCH_ARGS"
echo "Logging vers $LOG_DIR/console.txt..."

# Redirection de la sortie vers tmp/console.txt pour analyse
# Note: usage de stdbuf pour forcer le flush du log en cas de Ctrl+C
stdbuf -oL -eL docker run --rm --gpus all -v "$RES_DIR:/app/docs/benchmark-results" episteme-gpu $BENCH_ARGS 2>&1 | tee "$LOG_DIR/console.txt"

echo "Terminé ! Les résultats sont dans docs/benchmark-results/"
echo "Les logs de console sont dans $LOG_DIR/console.txt"
echo "Les logs de build sont dans $LOG_DIR/docker_build.log"
echo "Les diagnostics sont dans $LOG_DIR/diagnostic_output.txt"
