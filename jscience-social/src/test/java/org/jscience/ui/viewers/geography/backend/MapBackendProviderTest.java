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

package org.jscience.ui.viewers.geography.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Map Backend Providers.
 */
public class MapBackendProviderTest {

    @Test
    void testJavaFXMapBackendProvider() {
        JavaFXMapBackendProvider provider = new JavaFXMapBackendProvider();
        
        assertEquals("map", provider.getType());
        assertEquals("javafx_map", provider.getId());
        assertNotNull(provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.isAvailable()); // JavaFX is always available
        assertTrue(provider.getPriority() >= 0);
    }

    @Test
    void testGeoToolsBackendProvider() {
        GeoToolsBackendProvider provider = new GeoToolsBackendProvider();
        
        assertEquals("map", provider.getType());
        assertEquals("geotools", provider.getId());
        assertEquals("GeoTools", provider.getName());
        assertNotNull(provider.getDescription());
        // isAvailable depends on classpath
        assertTrue(provider.getPriority() > 0);
    }

    @Test
    void testOpenMapBackendProvider() {
        OpenMapBackendProvider provider = new OpenMapBackendProvider();
        
        assertEquals("map", provider.getType());
        assertEquals("openmap", provider.getId());
        assertEquals("OpenMap", provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.getPriority() > 0);
    }

    @Test
    void testUnfoldingMapBackendProvider() {
        UnfoldingMapBackendProvider provider = new UnfoldingMapBackendProvider();
        
        assertEquals("map", provider.getType());
        assertEquals("unfolding", provider.getId());
        assertEquals("Unfolding Maps", provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.getPriority() > 0);
    }

    @Test
    void testGoogleGeoChartBackendProvider() {
        GoogleGeoChartBackendProvider provider = new GoogleGeoChartBackendProvider();
        
        assertEquals("map", provider.getType());
        assertEquals("geochart", provider.getId());
        assertEquals("Google GeoChart", provider.getName());
        assertNotNull(provider.getDescription());
        assertTrue(provider.getPriority() > 0);
    }
}
