/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for PhylogeneticTreeReader.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
import org.jscience.biology.loaders.PhylogeneticTreeReader;

public class PhylogeneticTreeReaderTest {

    @Test
    public void testClassPresence() {
        assertNotNull(PhylogeneticTreeReader.class);
    }
}
