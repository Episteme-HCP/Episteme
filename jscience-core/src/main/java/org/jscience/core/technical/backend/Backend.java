package org.jscience.core.technical.backend;

import java.util.List;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Base interface for discoverable backend providers.
 * Used by ServiceLoader to dynamically discover available backends
 * (plotting, molecular visualization, computation, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Backend {

    /**
     * Returns the backend type category (e.g., "plotting", "molecular", "tensor",
     * "math").
     */
    String getType();

    /**
     * Returns the unique identifier for this backend (e.g., "javafx", "jmol",
     * "cuda").
     */
    String getId();

    /**
     * Returns the display name for UI presentation.
     */
    String getName();

    /**
     * Returns a description of the backend.
     */
    String getDescription();

    /**
     * Checks if this backend is currently available (libraries loaded, etc.).
     */
    boolean isAvailable();

    /**
     * Returns the priority for auto-selection (higher = preferred).
     * Used when multiple backends are available.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Creates and returns the backend instance (or returns self if it is the
     * backend).
     * 
     * @return The backend implementation object
     */
    Object createBackend();

    /**
     * Returns the {@link AlgorithmProvider}s exposed by this backend.
     * <p>
     * This bridges the Backend discovery system (ServiceLoader&lt;Backend&gt;)
     * with the Algorithm discovery system (ServiceLoader&lt;AlgorithmProvider&gt;),
     * allowing {@code AlgorithmManager} to find providers registered through
     * either path.
     * </p>
     * <p>
     * Default implementation checks if {@link #createBackend()} returns an
     * AlgorithmProvider instance.
     * </p>
     *
     * @return list of algorithm providers (may be empty, never null)
     */
    default List<AlgorithmProvider> getAlgorithmProviders() {
        Object backend = createBackend();
        if (backend instanceof AlgorithmProvider) {
            return List.of((AlgorithmProvider) backend);
        }
        return List.of();
    }
}


