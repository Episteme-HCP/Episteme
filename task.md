---
description: Plan de développement détaillé pour JScience Phase 5+
---

# 🚀 JScience - Plan de Développement Phase 5 & Optimisations

Ce plan consolide les actions requises pour aligner JScience sur les standards HPC (High Performance Computing) modernes, en mettant l'accent sur la performance native (SIMD, BLAS) et le vrai calcul distribué (MPI).

## ✅ 1. État des Lieux et Prérequis

- [x] **Vérification Java Version** : Le projet doit être compilé avec Java 21+.
- [x] **Flag Incubator** : Ajouter `--add-modules jdk.incubator.vector` aux configurations de build (Maven) et de run.

## ⚡ 2. Accélération SIMD (Vector API)

*Objectif : Exploiter AVX-512 / NEON pour les boucles critiques.*

- [x] **Créer `SIMDDoubleMatrix` (jscience-core)** :
    - Une implémentation de `Matrix<Real>` optimisée.
    - [x] **Opérations Élémentaires** : `add`, `sub`, `mul`, `div`.
    - [x] **Opérations Transcendantes** : `sqrt`, `exp`, `log`, `sin`, `cos`, `abs` vectorisés via `VectorOperators`.
- [x] **Intégrer dans le Planner** :
    - Les factories privilégient désormais cette implémentation pour les matrices réelles.

## 💎 3. Native BLAS Provider

*Objectif : Performance maximale sur CPU via OpenBLAS / Intel MKL.*

- [x] **Créer `NativeLinearAlgebraProvider` (jscience-native)** :
  - Détecter la présence de bibliothèques partagées.
  - [x] **Routines Supportées** : `DGEMM` (Multiplication), `DSCAL` (Échelle), `DAXPY` (Addition pondérée).
  - Utiliser **Project Panama (FFM API)** pour charger dynamiquement les symboles sans dépendance forte.
- [x] **Benchmarking** :
  - Comparaison possible entre `NativeBLAS`, `SIMD` et `Pur Java`.

## 🌐 4. MPI Binding & Algos Avancés

*Objectif : Vrai distribué et évitement de communication.*

- [x] **Refactor `MPIDistributedContext`** :
    - Nettoyage de `MpiStrategy` et documentation des limitations du `submit` dynamique (SPMD vs MPMD).
    - [x] **Algorithmes HPC** :
    - [x] **2.5D Algorithm** (`Algorithm25D`) pour limiter les communications réseau.
    - [x] **CARMA Algorithm** :
        - [x] `RealAlgorithmCARMA` : Version haute précision (Arbitrary precision).
        - [x] `RealDoubleAlgorithmCARMA` : Version haute performance (SIMD/double primitive).
- [x] **Support LAPACK Natif** :
    - [x] Inversion de matrice via `DGETRF` et `DGETRI`.
    - [x] Résolution de systèmes linéaires via `DGESV`.

## 🧹 5. Audit et Packaging

- [x] **Audit Qualité** : Nettoyage de la dette technique dans `HDF5Reader` et `MMapMatrix` (champs/classes inutilisés).
- [ ] **Fat Jars & Modules** : S'assurer que les connecteurs natifs (Panama) fonctionnent une fois packagés.
- [x] **Documentation** : README mis à jour avec les prérequis système (OpenBLAS/MPI).
