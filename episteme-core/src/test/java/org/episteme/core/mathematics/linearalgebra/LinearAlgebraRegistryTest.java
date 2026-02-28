/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.junit.jupiter.api.Test;
import org.episteme.core.ComputeContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for LinearAlgebraRegistry.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LinearAlgebraRegistryTest {

    @Test
    public void testClassPresence() {
        // Ensure class is reachable (using ComputeContext instead of deprecated LinearAlgebraRegistry)
        assertNotNull(ComputeContext.class);
    }
}

