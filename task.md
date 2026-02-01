---
description: Plan de développement détaillé pour JScience Phase 5+
---

# 🚀 JScience - Plan de Développement Phase 5 & Optimisations

Ce plan consolide les actions requises pour aligner JScience sur les standards HPC (High Performance Computing) modernes, en mettant l'accent sur la performance native (SIMD, BLAS) et le vrai calcul distribué (MPI).

## ✅ 1. État des Lieux et Prérequis

-   [ ] **Vérification Java Version** : Le projet doit être compilé avec Java 21+.
-   [ ] **Flag Incubator** : Ajouter `--add-modules jdk.incubator.vector` aux configurations de build (Maven) et de run, car la Vector API est toujours en incubation en Java 21+.

## ⚡ 2. Accélération SIMD (Vector API)

*Objectif : Exploiter AVX-512 / NEON pour les boucles critiques.*

-   [ ] **Créer `VectorSimdMatrix` (jscience-core)** :
    -   Une implémentation de `Matrix<Real>` (ou `RealDoubleMatrix`) optimisée.
    -   Utiliser `DoubleVector.fromArray(...)` pour charger des blocs de données.
    -   Implémenter `add`, `sub`, `mul` (pointwise) avec les opérations vectorielles.
-   [ ] **Intégrer dans le Planner** :
    -   Modifier les factories de matrices pour privilégier cette implémentation si le module `jdk.incubator.vector` est disponible.

##  3. Native BLAS Provider

*Objectif : Performance maximale sur CPU via OpenBLAS / Intel MKL.*

-   [x] **Créer `NativeLinearAlgebraProvider` (jscience-native)** :
    -   Détecter la présence de bibliothèques partagées (`libopenblas.so`, `mkl_rt.dll`).
    -   Utiliser **Project Panama (FFM API)** pour charger dynamiquement les symboles (`dgemm_`, `dgesv_`, etc.).
    -   *Architecture* : Ne pas créer de dépendance forte. Si la lib est absente, le constructeur lève une exception (ou retourne false) et JScience continue avec ses algos Java.
-   [x] **Benchmarking** :
    -   Comparer `NativeLinearAlgebraProvider` vs `VectorSimdMatrix` vs `Standard Java`.
-   [x] **Scripts de Lancement** :
    -   Mise à jour de `start_cpu.bat` et `run_benchmarks.bat` pour injecter OpenBLAS dans le PATH.

## 🌐 4. MPI Binding (Vrai Distribué)

*Objectif : Passer de la simulation locale au cluster physique.*

-   [ ] **Refactor `MPIDistributedContext`** :
    -   Il existe déjà une détection MPI par réflexion dans `org.jscience.core.distributed.MPIDistributedContext`.
    -   **Audit** : Vérifier que l'implémentation actuelle (`MpiStrategy`) est fonctionnelle et couvre les besoins des nouveaux algos (2.5D, CARMA).
    -   Déplacer si nécessaire la logique purement native dans `jscience-native` pour isoler les dépendances JNI/MPI.

## 📦 5. Packaging et CI

-   [ ] **Fat Jars & Modules** : S'assurer que les connecteurs natifs (Panama) fonctionnent une fois packagés.
-   [ ] **Documentation** : Mettre à jour le README avec les prérequis système (Installer OpenBLAS/MPI) pour activer ces fonctionnalités "Turbo".
