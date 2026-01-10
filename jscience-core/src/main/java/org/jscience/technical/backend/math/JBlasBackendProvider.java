/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * BackendProvider for JBlas.
 */
public class JBlasBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_MATH;
    }

    @Override
    public String getId() {
        return "jblas";
    }

    @Override
    public String getName() {
        return "JBlas";
    }

    @Override
    public String getDescription() {
        return "Linear algebra for Java based on BLAS/LAPACK.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jblas.NativeBlas");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public Object createBackend() {
        if (isAvailable()) {
            return new org.jscience.mathematics.linearalgebra.backends.JBlasLinearAlgebraProvider<>();
        }
        return null;
    }
}

