/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.vision;

import org.episteme.core.technical.backend.AbstractBackendManager;

/**
 * Manager for {@link VisionAlgorithmBackend} instances.
 * <p>
 * Provides standardized discovery and access to computer vision backends.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class VisionBackendManager extends AbstractBackendManager<VisionAlgorithmBackend<?>> {

    private static final VisionBackendManager INSTANCE = new VisionBackendManager();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private VisionBackendManager() {
        super((Class) VisionAlgorithmBackend.class);
    }

    /**
     * Returns the global singleton instance.
     * 
     * @return the VisionBackendManager instance
     */
    public static VisionBackendManager getInstance() {
        return INSTANCE;
    }

    // ---- Static convenience methods ----

    /**
     * Returns the default vision backend.
     * 
     * @return the default backend
     */
    public static VisionAlgorithmBackend<?> staticDefault() {
        return INSTANCE.managerDefault();
    }

    /**
     * Selects a vision backend by name or ID.
     * 
     * @param nameOrId the backend name or ID
     * @return the selected backend
     */
    public static VisionAlgorithmBackend<?> staticSelect(String nameOrId) {
        return INSTANCE.managerSelect(nameOrId);
    }

    /**
     * Returns all registered vision backends.
     * 
     * @return collection of all backends
     */
    public static java.util.Collection<VisionAlgorithmBackend<?>> staticAll() {
        return INSTANCE.managerAll();
    }
}
