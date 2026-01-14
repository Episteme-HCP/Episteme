/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserPreferences.
 */
public class UserPreferencesTest {

    @Test
    void testGetInstance() {
        UserPreferences prefs = UserPreferences.getInstance();
        assertNotNull(prefs);
        
        // Singleton pattern
        UserPreferences prefs2 = UserPreferences.getInstance();
        assertSame(prefs, prefs2);
    }

    @Test
    void testSetAndGet() {
        UserPreferences prefs = UserPreferences.getInstance();
        
        String testKey = "test.key." + System.currentTimeMillis();
        String testValue = "testValue";
        
        prefs.set(testKey, testValue);
        assertEquals(testValue, prefs.get(testKey));
    }

    @Test
    void testGetWithDefault() {
        UserPreferences prefs = UserPreferences.getInstance();
        
        String nonExistentKey = "non.existent.key." + System.currentTimeMillis();
        String defaultValue = "defaultValue";
        
        assertEquals(defaultValue, prefs.get(nonExistentKey, defaultValue));
    }

    @Test
    void testBackendPreferences() {
        UserPreferences prefs = UserPreferences.getInstance();
        
        prefs.setPreferredBackend("map", "geotools");
        assertEquals("geotools", prefs.getPreferredBackend("map"));
        
        prefs.setPreferredBackend("network", "graphstream");
        assertEquals("graphstream", prefs.getPreferredBackend("network"));
    }

    @Test
    void testPreferencesPath() {
        UserPreferences prefs = UserPreferences.getInstance();
        Path path = prefs.getPreferencesPath();
        
        assertNotNull(path);
        assertTrue(path.toString().contains(".jscience"));
        assertTrue(path.toString().contains("preferences.properties"));
    }
}
