#!/bin/bash
# Episteme VM Maintenance & Benchmark Script
# - Nettoie l'espace disque (Docker + Maven)
# - Construit l'image GPU optimisée
# - Lance les benchmarks avec accès GPU

echo "--- [1/3] Nettoyage de l'espace disque ---"
# Nettoyage Docker (Images orphelines, containers arrêtés, cache inutilisé)
docker system prune -f --volumes

# Nettoyage Maven local (facultatif mais libère de la place si besoin)
# rm -rf ~/.m2/repository/org/episteme

echo "--- [2/3] Construction de l'image GPU ---"
# On force BuildKit pour le cache intelligent
export DOCKER_BUILDKIT=1
docker build -t episteme-gpu -f docker/Dockerfile.gpu .

echo "--- [3/3] Lancement des Benchmarks ---"
# Lance le container avec accès au GPU et génère le PDF
docker run --rm --gpus all -v "$(pwd)/docs/benchmark-results:/app/docs/benchmark-results" episteme-gpu --run-all --pdf

echo "Terminé ! Les résultats sont dans docs/benchmark-results/"
