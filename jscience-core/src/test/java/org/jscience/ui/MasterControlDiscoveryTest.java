/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MasterControlDiscoveryTest {

    @Test
    public void testFindApps() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> apps = discovery.findClasses("App");
        assertNotNull(apps);
        // We know JScienceDashboard exists
        assertFalse(apps.isEmpty(), "Should find at least one App");
    }

    @Test
    public void testFindDevices() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> devices = discovery.findClasses("Device");
        assertNotNull(devices);
        // Should find our local TestDevice
        boolean foundTest = devices.stream().anyMatch(d -> d.simpleName.equals("TestDevice"));
        assertTrue(foundTest, "Should find TestDevice");
    }

    @Test
    public void testSortingAndDeDuplication() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> loaders = discovery.findClasses("Loader");

        // Check sorting
        for (int i = 0; i < loaders.size() - 1; i++) {
            assertTrue(loaders.get(i).simpleName.compareTo(loaders.get(i + 1).simpleName) <= 0);
        }

        // Check de-duplication
        long uniqueFullNames = loaders.stream().map(l -> l.fullName).distinct().count();
        assertEquals(loaders.size(), uniqueFullNames, "List should not have duplicate class names");
    }
}
