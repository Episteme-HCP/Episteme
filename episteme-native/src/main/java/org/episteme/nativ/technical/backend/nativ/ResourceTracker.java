/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A "Native-Safe" resource tracker for deterministic cleanup of native resources.
 * Supports JoCL objects (cl_mem, cl_kernel, etc.) and Panama MemorySegments (e.g., FFTW plans).
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class ResourceTracker implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ResourceTracker.class);
    private final List<Resource<?>> resources = new ArrayList<>();

    /**
     * Registers a native resource with a custom cleanup action.
     */
    public <T> T track(T handle, Consumer<T> cleaner) {
        if (handle != null) {
            resources.add(new Resource<>(handle, cleaner));
        }
        return handle;
    }

    /**
     * Specialized tracker for FFTW plans (MemorySegments).
     */
    public MemorySegment trackPlan(MemorySegment plan, Consumer<MemorySegment> cleaner) {
        if (plan != null && !plan.equals(MemorySegment.NULL)) {
            resources.add(new Resource<>(plan, cleaner));
        }
        return plan;
    }

    @Override
    public void close() {
        // Close resources in reverse order of allocation
        for (int i = resources.size() - 1; i >= 0; i--) {
            Resource<?> resource = resources.get(i);
            try {
                resource.release();
            } catch (Throwable t) {
                logger.error("Failed to release native resource: {}", t.getMessage());
            }
        }
        resources.clear();
    }

    private static class Resource<T> {
        private final T handle;
        private final Consumer<T> cleaner;
        private boolean released = false;

        private Resource(T handle, Consumer<T> cleaner) {
            this.handle = handle;
            this.cleaner = cleaner;
        }

        private void release() {
            if (!released) {
                cleaner.accept(handle);
                released = true;
            }
        }
    }
}
