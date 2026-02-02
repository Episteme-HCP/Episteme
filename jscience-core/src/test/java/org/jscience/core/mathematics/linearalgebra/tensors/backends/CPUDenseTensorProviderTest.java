/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.tensors.backends;

import org.jscience.core.technical.algorithm.linearalgebra.CPUDenseTensorProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for CPUDenseTensorProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUDenseTensorProviderTest {

    @Test
    public void testClassPresence() {
        // Ensure class is reachable
        assertNotNull(CPUDenseTensorProvider.class);
    }
}

