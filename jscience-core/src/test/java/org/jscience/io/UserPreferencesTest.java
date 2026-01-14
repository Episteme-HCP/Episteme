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
