package org.jscience.mathematics.linearalgebra;

import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.mathematics.linearalgebra.tensors.backends.TensorProvider;
import org.jscience.mathematics.linearalgebra.tensors.backends.CPUDenseTensorProvider;
import org.jscience.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider;

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.stream.StreamSupport;

/**
 * Registry for Linear Algebra providers (Matrices and Tensors).
 * <p>
 * This class serves as the central point for discovering and selecting
 * implementation providers.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 5.0
 */
public final class LinearAlgebraRegistry {

    private static final List<LinearAlgebraProvider<?>> MATRIX_PROVIDERS = new CopyOnWriteArrayList<>();
    private static final List<TensorProvider> TENSOR_PROVIDERS = new CopyOnWriteArrayList<>();

    // Default providers
    private static final TensorProvider DEFAULT_TENSOR_PROVIDER = new CPUDenseTensorProvider();

    static {
        // Load providers from SPI
        reload();
    }

    private LinearAlgebraRegistry() {
        // Utility class
    }

    /**
     * Reloads all providers using ServiceLoader.
     */
    public static synchronized void reload() {
        MATRIX_PROVIDERS.clear();
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> matrixLoader = ServiceLoader.load(LinearAlgebraProvider.class);
        StreamSupport.stream(matrixLoader.spliterator(), false).forEach(MATRIX_PROVIDERS::add);

        TENSOR_PROVIDERS.clear();
        ServiceLoader<TensorProvider> tensorLoader = ServiceLoader.load(TensorProvider.class);
        StreamSupport.stream(tensorLoader.spliterator(), false).forEach(TENSOR_PROVIDERS::add);

        // Ensure defaults are available if nothing else
        if (TENSOR_PROVIDERS.stream().noneMatch(p -> p instanceof CPUDenseTensorProvider)) {
            TENSOR_PROVIDERS.add(DEFAULT_TENSOR_PROVIDER);
        }
        // Matrix provider default handling might be tricky due to Field requirement in
        // provider constructor
        // But listing them is independent of Field.
    }

    /**
     * Returns all registered linear algebra providers.
     */
    public static List<LinearAlgebraProvider<?>> getMatrixProviders() {
        return Collections.unmodifiableList(MATRIX_PROVIDERS);
    }

    /**
     * Returns all registered tensor providers.
     */
    public static List<TensorProvider> getTensorProviders() {
        return Collections.unmodifiableList(TENSOR_PROVIDERS);
    }

    /**
     * Returns a matrix provider compatible with the given field.
     * 
     * @param field the field
     * @return a compatible provider
     */
    public static <E> LinearAlgebraProvider<E> getMatrixProvider(
            org.jscience.mathematics.structures.rings.Field<E> field) {
        // Simple strategy: return the default CPU provider for now.
        // In a real implementation, we would check if a specific provider (like GPU)
        // supports this field.
        return new CPUDenseLinearAlgebraProvider<>(field);
    }

    /**
     * Gets the best available Tensor provider.
     * 
     * @return the preferred tensor provider
     */
    public static TensorProvider getTensorProvider() {
        // Simple strategy: return the first one (SPI order) or default
        // Could be enhanced with priority/capability check
        return TENSOR_PROVIDERS.isEmpty() ? DEFAULT_TENSOR_PROVIDER : TENSOR_PROVIDERS.get(0);
    }
}
