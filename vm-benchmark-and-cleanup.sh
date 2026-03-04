#!/bin/bash
# Episteme VM Maintenance & Benchmark Script
# - Nettoie l'espace disque (Docker + Maven)
# - Met à jour le code (Git pull avec gestion des conflits)
# - Construit l'image GPU optimisée
# - Lance les benchmarks avec accès GPU

echo "--- [1/4] État de l'espace disque & Nettoyage ---"
df -h / | grep /
# Nettoyage Docker (Images orphelines, containers arrêtés, cache inutilisé)
docker system prune -f --volumes
echo "Après nettoyage :"
df -h / | grep /

echo "--- [2/4] Mise à jour du code (Git) ---"
# Gestion des conflits locaux (stash automatique pour permettre le pull)
GIT_CHANGES=$(git status --porcelain)
if [ -n "$GIT_CHANGES" ]; then
    echo "Changements locaux détectés. Stash temporaire..."
    git stash
fi

git pull origin main

if [ -n "$GIT_CHANGES" ]; then
    echo "Réapplication des changements locaux (si possible)..."
    git stash pop
fi

echo "--- [3/4] Construction de l'image GPU ---"
# On force BuildKit pour le cache intelligent
export DOCKER_BUILDKIT=1
docker build -t episteme-gpu -f docker/Dockerfile.gpu .

echo "--- [4/4] Lancement des Benchmarks ---"
# S'assurer que le dossier de résultats et tmp existent
mkdir -p docs/benchmark-results
mkdir -p tmp

echo "Exécution des diagnostics..."
docker run --rm --gpus all episteme-gpu ./run-diagnostic.sh > tmp/diagnostic_output.txt 2>&1

echo "Lancement des benchmarks (Logging vers tmp/console.txt)..."
# Lance le container avec accès au GPU et génère le PDF
# Redirection de la sortie vers tmp/console.txt pour analyse
docker run --rm --gpus all -v "$(pwd)/docs/benchmark-results:/app/docs/benchmark-results" episteme-gpu --run-all --pdf 2>&1 | tee tmp/console.txt

echo "Terminé ! Les résultats sont dans docs/benchmark-results/"
echo "Les logs de console sont dans tmp/console.txt"
