package org.jscience.core.technical.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorProvider;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.MatrixStorageFactory;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.structures.rings.Ring;

/**
 * Registry and selector for Algorithm Providers.
 * Extracted from ComputeContext.
 */
public class ProviderRegistry {
 
    private static final Logger LOGGER = Logger.getLogger(ProviderRegistry.class.getName());

    private final Map<String, LinearAlgebraProvider<?>> providers = new ConcurrentHashMap<>();
    private static final List<MatrixStorageFactory> matrixStorageFactories = new ArrayList<>();

    static {
        try {
            Class<?> clazz = Class.forName("org.jscience.nativ.mathematics.linearalgebra.matrices.NativeMatrixStorageFactory");
            matrixStorageFactories.add((MatrixStorageFactory) clazz.getDeclaredConstructor().newInstance());
        } catch (Throwable t) {}
    }

    public <E> void register(String name, LinearAlgebraProvider<E> provider) {
        providers.put(name, provider);
    }

    @SuppressWarnings("unchecked")
    public <E> LinearAlgebraProvider<E> get(String name) {
        return (LinearAlgebraProvider<E>) providers.get(name);
    }

    /**
     * Gets a linear algebra provider suited for the given operation context and ring.
     */
    @SuppressWarnings("unchecked")
    public <E> LinearAlgebraProvider<E> selectLinearAlgebraProvider(OperationContext ctx, Ring<E> ring) {
        try {
            // Use ProviderSelector to pick best scoring provider
            LinearAlgebraProvider<E> best = ProviderSelector.select(LinearAlgebraProvider.class, ctx);
            if (best.isCompatible(ring)) {
                return best;
            }
            
            // If best is not compatible, iterate for a compatible one
             List<?> candidates = AlgorithmManager.getProviders(LinearAlgebraProvider.class);
             for (Object obj : candidates) {
                 LinearAlgebraProvider<?> p = (LinearAlgebraProvider<?>) obj;
                 LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
                 if (typedProvider.isCompatible(ring)) {
                     return typedProvider;
                 }
             }
        } catch (Exception e) {
            LOGGER.warning("Failed to select best LinearAlgebraProvider: " + e.getMessage());
        }

        // Fallback: Pick whatever is available via AlgorithmManager instead of hardcoded new instance
        try {
            return AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        } catch (NoSuchElementException e) {
            // Last resort
            Field<E> field = (Field<E>) ring;
            return new CPUDenseLinearAlgebraProvider<E>(field);
        }
    }

    /**
     * Gets a sparse linear algebra provider.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E> LinearAlgebraProvider<E> getSparseLinearAlgebraProvider(Ring<E> ring) {
        Class<SparseLinearAlgebraProvider> type = SparseLinearAlgebraProvider.class;
        List<SparseLinearAlgebraProvider> candidates = AlgorithmManager.getProviders(type);
        
        for (SparseLinearAlgebraProvider<?> p : candidates) {
            LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
            if (typedProvider.isCompatible(ring)) {
                return typedProvider;
            }
        }

        // Fallback to CPU Sparse if discovered
        try {
            return (LinearAlgebraProvider<E>) AlgorithmManager.getProvider(SparseLinearAlgebraProvider.class);
        } catch (NoSuchElementException e) {
            return new CPUSparseLinearAlgebraProvider<E>(ring);
        }
    }

    /**
     * Selects a tensor provider based on context.
     */
    public TensorProvider selectTensorProvider(OperationContext ctx) {
        return ProviderSelector.select(TensorProvider.class, ctx);
    }

    /**
     * Creates optimal storage based on data and density context.
     */
    public <E> MatrixStorage<E> createStorage(int rows, int cols, Ring<E> ring, double density) {
        if (density < 0.2) {
            return new SparseMatrixStorage<>(rows, cols, ring.zero());
        }

        // Specialized path for Real
        if (ring.getClass().getName().contains("Reals")) {
            @SuppressWarnings({"unchecked", "preview", "restricted"})
            MatrixStorage<E> specialized = (MatrixStorage<E>) (MatrixStorage<?>) new org.jscience.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage(rows, cols);
            return specialized;
        }
        
        // Try SPI factories (Native, CUDA, etc.)
        for (MatrixStorageFactory factory : matrixStorageFactories) {
            MatrixStorage<E> s = factory.createDense(rows, cols, ring);
            if (s != null) return s;
        }
        
        return new DenseMatrixStorage<>(rows, cols, ring.zero());
    }

    /**
     * Creates optimal vector storage.
     */
    public <E> VectorStorage<E> createVectorStorage(int dim, Ring<E> ring, double density) {
        if (density < 0.2) {
            return new SparseVectorStorage<E>(dim, ring.zero());
        }
        // Native vector storage could be added here similar to matrix
        return new DenseVectorStorage<E>(dim);
    }
}
