# Project Panama Integration Guide

## Introduction

Project Panama (Foreign Function & Memory API) permet d'appeler du code natif C/C++ directement depuis Java **sans JNI**, avec un surcoût minimal et un accès mémoire "zero-copy".

> [!IMPORTANT]
> Requiert **Java 22+** pour l'API stable (JEP 454).

---

## 1. Avantages vs JNI

| Aspect | JNI (Ancien) | Panama (Nouveau) |
| :--- | :--- | :--- |
| Code C++ requis | Oui (wrapper) | Non |
| Copie mémoire | Fréquente | Évitable (zero-copy) |
| Overhead appel | ~100ns | ~10ns |
| Génération bindings | Manuel | Auto (`jextract`) |

---

## 2. Architecture Proposée

### 2.1 Matrices "Panama-Ready"

```java
// Concept : matrices adossées à mémoire native
public class NativeMatrix implements AutoCloseable {
    private final MemorySegment data;  // Mémoire off-heap
    private final int rows, cols;
    
    public NativeMatrix(int rows, int cols, Arena arena) {
        this.rows = rows;
        this.cols = cols;
        this.data = arena.allocate(
            ValueLayout.JAVA_DOUBLE, 
            (long) rows * cols
        );
    }
    
    // Accès direct pour C++
    public MemorySegment segment() {
        return data;
    }
}
```

### 2.2 Appel BLAS via Panama

```java
// Bindings générés par jextract depuis cblas.h
public class BlasBackend {
    
    public static void dgemm(NativeMatrix A, NativeMatrix B, NativeMatrix C) {
        // Appel direct à la fonction C, zero-copy
        cblas_dgemm(
            CblasRowMajor, CblasNoTrans, CblasNoTrans,
            A.rows(), A.cols(), B.cols(),
            1.0,                        // alpha
            A.segment(), A.cols(),      // A et lda
            B.segment(), B.cols(),      // B et ldb
            0.0,                        // beta
            C.segment(), C.cols()       // C et ldc
        );
    }
}
```

---

## 3. Intégration HDF5

### 3.1 Lecture Zero-Copy

```java
public class HDF5PanamaLoader {
    
    public NativeMatrix loadDataset(Path file, String datasetPath) {
        try (Arena arena = Arena.ofConfined()) {
            // 1. Ouvrir fichier HDF5 via libhdf5
            MemorySegment fileId = H5Fopen(
                arena.allocateFrom(file.toString()),
                H5F_ACC_RDONLY, H5P_DEFAULT
            );
            
            // 2. Ouvrir dataset
            MemorySegment dsetId = H5Dopen2(fileId, 
                arena.allocateFrom(datasetPath), H5P_DEFAULT);
            
            // 3. Mapper directement en mémoire (zero-copy!)
            MemorySegment data = H5Dread_direct(dsetId, ...);
            
            return new NativeMatrix(data, rows, cols);
        }
    }
}
```

### 3.2 Avantages

- Fichiers > RAM supportés via memory mapping
- Aucune copie Java heap
- Accès natif aux fonctionnalités HDF5 avancées

---

## 4. Étapes d'Implémentation

### Phase 1 : Infrastructure

1. Ajouter dépendance `--enable-preview` pour Panama
2. Créer module `jscience-native`
3. Implémenter `NativeMatrix` de base

### Phase 2 : BLAS

1. Générer bindings avec `jextract -t org.jscience.native.blas cblas.h`
2. Implémenter `BlasBackend`
3. Intégrer comme provider optionnel dans `DenseMatrix`

### Phase 3 : HDF5

1. Générer bindings for `hdf5.h`
2. Implémenter `HDF5Reader`
3. Ajouter au module `jscience-natural` (loaders)

---

## 5. Fallback Strategy

```java
public interface MatrixBackend {
    void multiply(Matrix A, Matrix B, Matrix C);
}

// Sélection automatique
MatrixBackend backend = PanamaBLASBackend.isAvailable() 
    ? new PanamaBLASBackend()
    : new PureJavaBackend();
```

---

## Références

- [JEP 454: Foreign Function & Memory API](https://openjdk.org/jeps/454)
- [jextract Tool](https://github.com/openjdk/jextract)
- [CBLAS Reference](https://www.netlib.org/blas/)
