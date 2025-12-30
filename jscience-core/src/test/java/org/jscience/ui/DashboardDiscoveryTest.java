package org.jscience.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DashboardDiscoveryTest {

    @Test
    public void testFindApps() {
        DashboardDiscovery discovery = DashboardDiscovery.getInstance();
        List<DashboardDiscovery.ClassInfo> apps = discovery.findClasses("App");
        assertNotNull(apps);
        // We know JScienceDashboard exists
        assertFalse(apps.isEmpty(), "Should find at least one App");
    }

    @Test
    public void testFindDevices() {
        DashboardDiscovery discovery = DashboardDiscovery.getInstance();
        List<DashboardDiscovery.ClassInfo> devices = discovery.findClasses("Device");
        assertNotNull(devices);
        // Should find our local TestDevice
        boolean foundTest = devices.stream().anyMatch(d -> d.simpleName.equals("TestDevice"));
        assertTrue(foundTest, "Should find TestDevice");
    }

    @Test
    public void testSortingAndDeDuplication() {
        DashboardDiscovery discovery = DashboardDiscovery.getInstance();
        List<DashboardDiscovery.ClassInfo> loaders = discovery.findClasses("Loader");

        // Check sorting
        for (int i = 0; i < loaders.size() - 1; i++) {
            assertTrue(loaders.get(i).simpleName.compareTo(loaders.get(i + 1).simpleName) <= 0);
        }

        // Check de-duplication
        long uniqueFullNames = loaders.stream().map(l -> l.fullName).distinct().count();
        assertEquals(loaders.size(), uniqueFullNames, "List should not have duplicate class names");
    }
}
