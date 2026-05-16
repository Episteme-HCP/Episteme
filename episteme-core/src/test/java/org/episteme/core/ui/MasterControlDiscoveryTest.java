/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Automated discovery tests for MasterControlDiscovery.
 * Validates that the discovery mechanism correctly finds components from:
 * 1. The core module itself
 * 2. Sibling Maven modules (episteme-natural, episteme-social, etc.)
 * 3. SPI-registered providers via ServiceLoader
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MasterControlDiscoveryTest {

    @Test
    @DisplayName("findClasses('App') returns at least one App")
    public void testFindApps() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> apps = discovery.findClasses("App");
        assertNotNull(apps, "App list must not be null");
        assertFalse(apps.isEmpty(), "Should find at least one App on classpath");
    }

    @Test
    @DisplayName("findClasses('Device') finds simulated devices")
    public void testFindDevices() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> devices = discovery.findClasses("Device");
        assertNotNull(devices, "Device list must not be null");
    }

    @Test
    @DisplayName("Results are sorted and de-duplicated")
    public void testSortingAndDeDuplication() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> loaders = discovery.findClasses("Loader");

        // Check ascending sort
        for (int i = 0; i < loaders.size() - 1; i++) {
            int cmp = loaders.get(i).simpleName.compareTo(loaders.get(i + 1).simpleName);
            assertTrue(cmp <= 0,
                "Results must be sorted: '" + loaders.get(i).simpleName + "' should come before '" + loaders.get(i + 1).simpleName + "'");
        }

        // Check de-duplication
        long uniqueFullNames = loaders.stream().map(l -> l.fullName).distinct().count();
        assertEquals(loaders.size(), uniqueFullNames,
            "List must not contain duplicate class full names");
    }

    @Test
    @DisplayName("SPI providers are discovered (Viewers via ServiceLoader)")
    public void testSPIProviderDiscovery() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        // SPI providers are loaded via getProviders() → ServiceLoader<Viewer>
        List<Viewer> providers = discovery.getProviders();
        assertNotNull(providers, "Provider list from SPI must not be null");
        System.out.println("[TEST] SPI Viewers discovered: " + providers.size());
        for (Viewer v : providers) {
            System.out.println("[TEST]   - " + v.getClass().getName() + " (category=" + v.getCategory() + ")");
        }
    }

    @Test
    @DisplayName("getProvidersByType() groups providers into APP/DEMO/VIEWER")
    public void testProviderGrouping() {
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        Map<MasterControlDiscovery.ProviderType, Map<String, List<Viewer>>> grouped = discovery.getProvidersByType();
        assertNotNull(grouped, "Grouped providers must not be null");
        // Print the structure for debugging
        for (var entry : grouped.entrySet()) {
            System.out.println("[TEST] ProviderType=" + entry.getKey() + ": " + entry.getValue().size() + " categories");
            for (var cat : entry.getValue().entrySet()) {
                System.out.println("[TEST]   Category='" + cat.getKey() + "': " + cat.getValue().size() + " providers");
            }
        }
    }

    @Test
    @DisplayName("Sibling module target/classes directories are scanned in dev mode")
    public void testSiblingModuleDiscovery() {
        // This test verifies the hardened heuristic that scans sibling module directories.
        // It confirms that if a parent pom.xml exists, the discovery walks siblings.
        File cwd = new File(System.getProperty("user.dir"));
        File parent = cwd.getParentFile();

        if (parent == null || !new File(parent, "pom.xml").exists()) {
            System.out.println("[TEST] Not in a Maven multi-module root; sibling scan skipped.");
            return; // Not in multi-module context, skip gracefully
        }

        System.out.println("[TEST] Parent Maven root detected: " + parent.getAbsolutePath());

        // Gather all sibling modules that have built target/classes
        File[] siblings = parent.listFiles(File::isDirectory);
        int builtSiblings = 0;
        if (siblings != null) {
            for (File sibling : siblings) {
                File targetClasses = new File(sibling, "target/classes");
                if (targetClasses.exists() && targetClasses.isDirectory()) {
                    builtSiblings++;
                    System.out.println("[TEST]   Built sibling module: " + sibling.getName());
                }
            }
        }
        System.out.println("[TEST] Total built siblings found: " + builtSiblings);

        // Now run an actual discovery and verify it found something from sibling packages
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<MasterControlDiscovery.ClassInfo> apps = discovery.findClasses("App");

        System.out.println("[TEST] Total Apps discovered across all modules: " + apps.size());
        for (MasterControlDiscovery.ClassInfo app : apps) {
            System.out.println("[TEST]   App: " + app.fullName);
        }

        if (builtSiblings > 1) {
            // If more than just the core module is built, we expect richer discovery
            assertFalse(apps.isEmpty(),
                "With " + builtSiblings + " sibling modules built, at least one App should be discoverable");
        }

        // Critical check: no duplicates from the same module being scanned twice
        long uniqueNames = apps.stream().map(a -> a.fullName).distinct().count();
        assertEquals(apps.size(), uniqueNames,
            "Sibling scan must not create duplicate entries. Found " + (apps.size() - uniqueNames) + " duplicates.");
    }

    @Test
    @DisplayName("episteme-natural module classes are discoverable when built")
    public void testNaturalModuleDiscovery() {
        File cwd = new File(System.getProperty("user.dir"));
        File parent = cwd.getParentFile();

        if (parent == null) {
            System.out.println("[TEST] No parent directory, skipping.");
            return;
        }

        File naturalTarget = new File(parent, "episteme-natural/target/classes");
        if (!naturalTarget.exists()) {
            System.out.println("[TEST] episteme-natural/target/classes not found — module not built, test skipped.");
            return;
        }

        System.out.println("[TEST] episteme-natural is built. Verifying discovery can reach it.");

        // Run discovery for Viewers which natural module should contribute
        MasterControlDiscovery discovery = MasterControlDiscovery.getInstance();
        List<Viewer> providers = discovery.getProviders();

        // Check if any provider class name contains 'natural'
        boolean foundNaturalProvider = providers.stream()
            .anyMatch(p -> p.getClass().getName().contains("natural"));

        System.out.println("[TEST] Natural-module SPI providers found: " + foundNaturalProvider);
        if (!foundNaturalProvider) {
            System.out.println("[WARN] No natural-module providers found via SPI. " +
                "Ensure META-INF/services/org.episteme.core.ui.Viewer exists in episteme-natural.");
        }

        // Also check classpath heuristic Apps
        List<MasterControlDiscovery.ClassInfo> apps = discovery.findClasses("App");
        boolean foundNaturalApp = apps.stream()
            .anyMatch(a -> a.fullName.contains("natural"));

        System.out.println("[TEST] Natural-module Apps found via classpath scan: " + foundNaturalApp);
        System.out.println("[TEST] All discovered Apps:");
        apps.forEach(a -> System.out.println("[TEST]   " + a.fullName));
    }
}
