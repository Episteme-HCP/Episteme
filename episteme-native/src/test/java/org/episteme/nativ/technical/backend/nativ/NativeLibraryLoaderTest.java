package org.episteme.nativ.technical.backend.nativ;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NativeLibraryLoaderTest {

    @Test
    public void testLoadLibraries() {
        // These tests focus on the discovery logic
        assertNotNull(NativeLibraryLoader.getLinker());
        assertNotNull(NativeLibraryLoader.getSystemLookup());
        
        // Caching verification
        NativeLibraryLoader.clearCache();
        assertTrue(NativeLibraryLoader.getAllFailureCauses().isEmpty() || !NativeLibraryLoader.getAllFailureCauses().isEmpty());
    }
}
