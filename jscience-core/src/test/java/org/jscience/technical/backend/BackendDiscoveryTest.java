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

package org.jscience.technical.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BackendDiscoveryTest {

    @Test
    public void testSingleton() {
        BackendDiscovery instance1 = BackendDiscovery.getInstance();
        BackendDiscovery instance2 = BackendDiscovery.getInstance();
        assertSame(instance1, instance2, "BackendDiscovery should be a singleton");
    }

    @Test
    public void testGetProvidersCaching() {
        BackendDiscovery discovery = BackendDiscovery.getInstance();
        
        List<BackendProvider> providers1 = discovery.getProviders();
        assertNotNull(providers1);
        
        List<BackendProvider> providers2 = discovery.getProviders();
        assertSame(providers1, providers2, "getProviders() should return cached list");
    }

    @Test
    public void testGetProvidersByType() {
        BackendDiscovery discovery = BackendDiscovery.getInstance();
        
        // Use a type that likely has no providers or known providers, just checking logic
        // Assuming we have at least one provider in the classpath (e.g. from tests or core)
        // If not, list is empty, but method should not fail.
        List<BackendProvider> list = discovery.getProvidersByType("unknown-type-xyz");
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testConstants() {
        assertEquals("plotting", BackendDiscovery.TYPE_PLOTTING);
        assertEquals("molecular", BackendDiscovery.TYPE_MOLECULAR);
        assertEquals("math", BackendDiscovery.TYPE_MATH);
        assertEquals("tensor", BackendDiscovery.TYPE_TENSOR);
        assertEquals("quantum", BackendDiscovery.TYPE_QUANTUM);
    }
}
