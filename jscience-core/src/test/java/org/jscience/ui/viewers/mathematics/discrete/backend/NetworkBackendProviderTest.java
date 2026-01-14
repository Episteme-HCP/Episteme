/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
