/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.mathematics.discrete.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Network Backend Providers.
 */
public class NetworkBackendProviderTest {

    @Test
    void testJavaFXNetworkBackendProvider() {
        JavaFXNetworkBackendProvider provider = new JavaFXNetworkBackendProvider();
        
        assertEquals("network", provider.getType());
        assertEquals("javafx_network", provider.getId());
        assertNotNull(provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.isAvailable()); // JavaFX is always available
        assertTrue(provider.getPriority() >= 0);
    }

    @Test
    void testJGraphTBackendProvider() {
        JGraphTBackendProvider provider = new JGraphTBackendProvider();
        
        assertEquals("network", provider.getType());
        assertEquals("jgrapht", provider.getId());
        assertEquals("JGraphT", provider.getName());
        assertNotNull(provider.getDescription());
        // isAvailable depends on classpath
        assertTrue(provider.getPriority() > 0);
    }

    @Test
    void testGraphStreamBackendProvider() {
        GraphStreamBackendProvider provider = new GraphStreamBackendProvider();
        
        assertEquals("network", provider.getType());
        assertEquals("graphstream", provider.getId());
        assertEquals("GraphStream", provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.getPriority() > 0);
    }
}
