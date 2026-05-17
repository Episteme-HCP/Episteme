/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.backend.ComputeBackend;

/**
 * Interface for linear algebra backends.
 * Combines general backend metadata with linear algebra implementation details.
 *
 * @param <E> the element type supported by this backend (e.g., Real, Double)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface LinearAlgebraBackend<E> extends ComputeBackend, LinearAlgebraProvider<E> {

    @Override
    default String getType() {
        return "linearalgebra";
    }

    @Override
    default String getName() {
        return LinearAlgebraProvider.super.getName();
    }

    @Override
    default int getPriority() {
        return ComputeBackend.super.getPriority();
    }

    @Override
    default boolean isAvailable() {
        return ComputeBackend.super.isAvailable();
    }

    @Override
    default void shutdown() {
        ComputeBackend.super.shutdown();
    }
}
