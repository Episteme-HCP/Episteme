/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.technical.backend.AbstractBackendManager;
import java.util.Collection;
import java.util.Comparator;

/**
 * Manager for linear algebra backends.
 * <p>
 * Discovers and manages available linear algebra providers that implement 
 * the {@link LinearAlgebraBackend} interface.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@SuppressWarnings("rawtypes")
public class LinearAlgebraBackendManager extends AbstractBackendManager<LinearAlgebraBackend> {

    private static final LinearAlgebraBackendManager INSTANCE = new LinearAlgebraBackendManager();

    /**
     * Returns the singleton instance of the manager.
     */
    public static LinearAlgebraBackendManager getInstance() {
        return INSTANCE;
    }

    /**
     * Utility method to get all discovered backends.
     */
    public static Collection<LinearAlgebraBackend> staticAllBackends() {
        return INSTANCE.managerAll();
    }

    private String preferredId = "auto";

    private LinearAlgebraBackendManager() {
        super(LinearAlgebraBackend.class);
    }

    /**
     * Gets the preferred linear algebra backend ID.
     */
    public String getPreferredId() {
        return preferredId;
    }

    /**
     * Sets the preferred linear algebra backend ID.
     */
    public void setPreferredId(String id) {
        this.preferredId = id;
    }

    /**
     * Returns the active linear algebra backend based on preference and priority.
     */
    public LinearAlgebraBackend<?> getActiveBackend() {
        if ("auto".equalsIgnoreCase(preferredId)) {
            return selectBestBackend();
        }
        LinearAlgebraBackend<?> b = managerSelect(preferredId);
        return (b != null) ? b : selectBestBackend();
    }

    @Override
    protected LinearAlgebraBackend selectBestBackend() {
        return backends.values().stream()
                .filter(LinearAlgebraBackend::isAvailable)
                .max(Comparator.comparingInt(LinearAlgebraBackend::getPriority))
                .orElse(null);
    }
}
