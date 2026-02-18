package org.jscience.core.technical.algorithm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.algorithm.ProviderSelector;

/**
 * Registry and selector for Algorithm Providers.
 * Extracted from ComputeContext.
 */
public class ProviderRegistry {

    private final Map<String, LinearAlgebraProvider<?>> providers = new ConcurrentHashMap<>();

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
            // Log?
        }

        // Fallback: CPUDense
        Field<E> field = (Field<E>) ring;
        return new CPUDenseLinearAlgebraProvider<E>(field);
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

        // Fallback to CPU Sparse
        return new CPUSparseLinearAlgebraProvider<E>(ring);
    }
}
