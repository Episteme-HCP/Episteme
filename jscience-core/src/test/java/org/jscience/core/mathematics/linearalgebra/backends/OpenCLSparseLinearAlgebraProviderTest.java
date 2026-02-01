/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.providers.OpenCLSparseLinearAlgebraProvider;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for OpenCLSparseLinearAlgebraProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenCLSparseLinearAlgebraProviderTest {

    @Test
    public void testClassPresence() {
        // Ensure class is reachable
        assertNotNull(OpenCLSparseLinearAlgebraProvider.class);
    }
}

