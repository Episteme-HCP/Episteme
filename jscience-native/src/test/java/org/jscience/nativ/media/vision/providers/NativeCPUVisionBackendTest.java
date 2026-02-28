/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.providers;

import org.episteme.core.media.vision.ImageOp;
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.awt.image.BufferedImage;
import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NativeCPUVisionBackendTest {

    @Test
    void testProviderMetadata() {
        org.episteme.nativ.vision.backends.NativeCPUVisionBackend provider = new org.episteme.nativ.vision.backends.NativeCPUVisionBackend();
        assertEquals("native-cpu-vision", provider.getId());
        assertEquals("vision", provider.getType());
        assertNotNull(provider.getDescription());
    }

    @Test
    void testIsAvailableDefaultsToFalse() {
        // Since episteme_vision.dll is not present
        org.episteme.nativ.vision.backends.NativeCPUVisionBackend provider = new org.episteme.nativ.vision.backends.NativeCPUVisionBackend();
        assertFalse(provider.isAvailable(), "Provider should not be available without native library");
    }

    @Test
    void testApplyDelegates() {
        org.episteme.nativ.vision.backends.NativeCPUVisionBackend provider = new org.episteme.nativ.vision.backends.NativeCPUVisionBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        
        // Define a simple Java Op
        ImageOp<BufferedImage> op = input -> input; // Identity
        
        BufferedImage result = provider.apply(img, op);
        assertSame(img, result, "Apply should delegate to ImageOp.process");
    }

    @Test
    void testProcessNativeThrowsException() {
        org.episteme.nativ.vision.backends.NativeCPUVisionBackend provider = new org.episteme.nativ.vision.backends.NativeCPUVisionBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        
        // Should throw because library is not loaded
        assertThrows(UnsupportedOperationException.class, () -> {
            provider.processNative(img, 1);
        });
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testNativeLibraryLoaderFindsKernel32() {
        // Verify that our loader logic actually works by trying to load a system library
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("Kernel32", Arena.global());
        assertTrue(lib.isPresent(), "Should be able to load Kernel32 on Windows");
        
        if (lib.isPresent()) {
            SymbolLookup lookup = lib.get();
            // GetTickCount is a simple function in Kernel32
            assertTrue(lookup.find("GetTickCount").isPresent(), "Should find GetTickCount symbol");
        }
    }
}
