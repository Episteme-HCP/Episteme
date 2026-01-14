/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
